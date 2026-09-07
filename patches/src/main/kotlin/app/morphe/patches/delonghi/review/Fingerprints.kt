package app.morphe.patches.delonghi.review

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall

/**
 * Matches HomeScreenActivity.Y(List) in De'Longhi Comfort 5.1.5.
 *
 * This method only checks eligibility and shows the app-rating dialog. Match its
 * session check and rating resources instead of the obfuscated method name.
 * Recheck the resource IDs against the APK before adding supported versions.
 */
internal object ReviewRequestDialogFingerprint : Fingerprint(
    definingClass = "Lcom/ddsx_ayla_android/view/comfort_v4/activity/HomeScreenActivity;",
    returnType = "V",
    parameters = listOf("Ljava/util/List;"),
    filters = listOf(
        methodCall(
            smali = "Lcom/ddsx_ayla_android/manager/application/MyApplication;->getManageAppSessionCount()I",
        ),
        literal(0x7f14037f), // POPUP_APPRATING_RATENOW_TITLE
        literal(0x7f140380), // POPUP_APPRATING_RATENOW_TXT
        literal(0x7f14037d), // POPUP_APPRATING_RATENOW_BTN
        literal(0x7f14037e), // POPUP_APPRATING_RATENOW_CLOSE
        methodCall(
            smali = "Lcom/ddsx_ayla_android/view/comfort_v4/dialogs/AllAlertDialogs;->showAlertDialogMessage(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/ddsx_ayla_android/view/comfort_v4/dialogs/AllAlertDialogs${'$'}DialogAlertDialogMessageCallback;)Landroidx/appcompat/app/AlertDialog;",
        ),
    ),
)
