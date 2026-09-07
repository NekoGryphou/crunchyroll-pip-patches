package app.morphe.patches.delonghi.review

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.delonghi.shared.COMPATIBILITY_DELONGHI_COMFORT

@Suppress("unused")
val disableReviewRequestDialogPatch = bytecodePatch(
    name = "Disable review request dialog",
    description = "Prevents De'Longhi Comfort from asking to rate the app.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_DELONGHI_COMFORT)

    execute {
        ReviewRequestDialogFingerprint.method.addInstructions(0, "return-void")
    }
}
