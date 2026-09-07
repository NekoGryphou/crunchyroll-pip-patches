package app.morphe.patches.delonghi.restore

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

internal const val PAC_FRAGMENT =
    "Lcom/ddsx_ayla_android/view/comfort_v4/fragments/devices/pac/PacApplianceFragment;"
internal const val APP_VIEW_MODEL =
    "Lcom/ddsx_ayla_android/view/comfort_v4/viewmodel/AppMainViewModel;"
internal const val BASE_FRAGMENT =
    "Lcom/ddsx_ayla_android/view/comfort_v4/base/NewBaseFragment;"

/** Restore selection before the view's lifecycle activates its observers. */
internal object PacViewCreatedFingerprint : Fingerprint(
    definingClass = PAC_FRAGMENT,
    returnType = "V",
    parameters = listOf("Landroid/view/View;", "Landroid/os/Bundle;"),
    filters = listOf(
        methodCall(smali = "Landroidx/fragment/app/Fragment;->getArguments()Landroid/os/Bundle;"),
        methodCall(smali = "$APP_VIEW_MODEL->onSingleDeviceSelected()Landroidx/lifecycle/LiveData;"),
        methodCall(
            smali = "Lcom/ddsx_ayla_android/base/AylaViewModel;->onFirmwareJobStatusChange()Landroidx/lifecycle/LiveData;",
        ),
        methodCall(
            smali = "Landroidx/lifecycle/LiveData;->observe(Landroidx/lifecycle/LifecycleOwner;Landroidx/lifecycle/Observer;)V",
        ),
        opcode(Opcode.RETURN_VOID, MatchAfterImmediately()),
    ),
)

/** Matches onResume(); v0-v2 are available immediately after the super call. */
internal object PacResumeFingerprint : Fingerprint(
    definingClass = PAC_FRAGMENT,
    returnType = "V",
    parameters = emptyList(),
    filters = listOf(
        methodCall(smali = "$BASE_FRAGMENT->onResume()V"),
        methodCall(smali = "$APP_VIEW_MODEL->getSelectedDeviceName()Ljava/lang/String;"),
        methodCall(smali = "Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V"),
    ),
)

/**
 * Matches X1(String), the selected-device observer in 5.1.5. Its unguarded
 * assignment overwrites the ID restored from fragment arguments with the new
 * ViewModel's initial empty selection. Do not match the obfuscated method name.
 */
internal object PacSelectedDeviceFingerprint : Fingerprint(
    definingClass = PAC_FRAGMENT,
    returnType = "V",
    parameters = listOf("Ljava/lang/String;"),
    filters = listOf(
        methodCall(
            smali = "Lcom/ddsx_ayla_android/view/comfort_v4/viewmodel/pac/PenguinDeviceViewModel;->getReelFeelColor(Ljava/lang/String;)Ljava/lang/String;",
        ),
        fieldAccess(smali = "$PAC_FRAGMENT->x:Ljava/lang/String;"),
        methodCall(
            smali = "Lcom/ddsx_ayla_android/view/comfort_v4/viewmodel/pac/BasePacDeviceViewModel;->getPacApplianceModal(Ljava/lang/String;)Lcom/ddsx_ayla_android/util/constants/PenguinProperty${'$'}PENGUIN_DEVICE_MODEL_TYPES;",
        ),
    ),
)

/**
 * Matches U1(Pair), the device connectivity observer. The online branch already
 * reloads properties; its return is a safe place to recheck normal permissions
 * if the device was unavailable when onResume restored the selection.
 */
internal object PacDeviceOnlineFingerprint : Fingerprint(
    definingClass = PAC_FRAGMENT,
    returnType = "V",
    parameters = listOf("Landroid/util/Pair;"),
    filters = listOf(
        methodCall(smali = "$PAC_FRAGMENT->getSelectedDeviceDsn()Ljava/lang/String;"),
        methodCall(smali = "Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z"),
        methodCall(smali = "Ljava/lang/Boolean;->booleanValue()Z"),
        methodCall(
            smali = "Lcom/ddsx_ayla_android/view/comfort_v4/viewmodel/pac/BasePacDeviceViewModel;->updateSelectedDeviceProperties(Ljava/lang/String;)V",
        ),
        opcode(Opcode.RETURN_VOID, MatchAfterImmediately()),
    ),
)
