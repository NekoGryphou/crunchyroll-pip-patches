package app.morphe.patches.delonghi.restore

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.delonghi.shared.COMPATIBILITY_DELONGHI_COMFORT

@Suppress("unused")
val restoreAcControlStatePatch = bytecodePatch(
    name = "Restore AC controls on return",
    description = "Restores the selected air conditioner and reloads its controls when returning to De'Longhi Comfort.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_DELONGHI_COMFORT)

    execute {
        // All parameters are references and all four methods are instance methods.
        // Fail on an unexpected register layout instead of overwriting parameters.
        listOf(
            PacViewCreatedFingerprint to 3,
            PacResumeFingerprint to 3,
            PacSelectedDeviceFingerprint to 1,
            PacDeviceOnlineFingerprint to 2,
        ).forEach { (fingerprint, requiredLocals) ->
            val method = fingerprint.method
            val locals = (method.implementation?.registerCount ?: 0) - method.parameters.size - 1
            if (locals < requiredLocals) {
                throw PatchException("Unexpected AC control register layout in ${method.name}.")
            }
        }

        // Empty selection events also occur when navigating back to Home. Ignore
        // them here without changing the Home screen's shared selection behavior.
        PacSelectedDeviceFingerprint.method.addInstructions(
            0,
            """
                invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z
                move-result v0
                if-eqz v0, :morphe_pac_selection_valid
                return-void
                :morphe_pac_selection_valid
                nop
            """,
        )

        // Use the fragment's navigation arguments, which survive process death.
        // Run before observers activate, and again after super.onResume().
        // setSingleDeviceSelected uses the app's normal permission checks, and
        // updateAylaDevicePowerStatus invokes its existing connectivity observer.
        val restoreSelectionInstructions = """
            invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getArguments()Landroid/os/Bundle;
            move-result-object v0
            if-eqz v0, :morphe_pac_resume_done
            sget-object v1, Lcom/ddsx_ayla_android/util/constants/AppConstants${'$'}AppIntentBundleKeys;->DEVICE_DSN_NUMBER:Ljava/lang/String;
            invoke-virtual {v0, v1}, Landroid/os/BaseBundle;->getString(Ljava/lang/String;)Ljava/lang/String;
            move-result-object v1
            invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z
            move-result v2
            if-nez v2, :morphe_pac_resume_done
            invoke-virtual {p0}, $BASE_FRAGMENT->getViewModel()Lcom/ddsx_ayla_android/base/BaseViewModel;
            move-result-object v0
            check-cast v0, $APP_VIEW_MODEL
            invoke-virtual {v0, v1}, $APP_VIEW_MODEL->setSingleDeviceSelected(Ljava/lang/String;)V
            invoke-virtual {v0, v1}, $APP_VIEW_MODEL->updateAylaDevicePowerStatus(Ljava/lang/String;)V
            :morphe_pac_resume_done
            nop
        """
        PacViewCreatedFingerprint.let { fingerprint ->
            fingerprint.method.addInstructions(
                fingerprint.instructionMatches.last().index,
                restoreSelectionInstructions,
            )
        }
        PacResumeFingerprint.let { fingerprint ->
            fingerprint.method.addInstructions(
                fingerprint.instructionMatches.first().index + 1,
                restoreSelectionInstructions,
            )
        }

        // A cold start can resume before Ayla has loaded the device. Recheck
        // access when its matching online event arrives; never force write access.
        PacDeviceOnlineFingerprint.let { fingerprint ->
            fingerprint.method.addInstructions(
                fingerprint.instructionMatches.last().index,
                """
                    invoke-virtual {p0}, $BASE_FRAGMENT->getViewModel()Lcom/ddsx_ayla_android/base/BaseViewModel;
                    move-result-object v0
                    check-cast v0, $APP_VIEW_MODEL
                    invoke-direct {p0}, $PAC_FRAGMENT->getSelectedDeviceDsn()Ljava/lang/String;
                    move-result-object v1
                    invoke-virtual {v0, v1}, $APP_VIEW_MODEL->checkIsUserHasWritePermission(Ljava/lang/String;)V
                """,
            )
        }
    }
}
