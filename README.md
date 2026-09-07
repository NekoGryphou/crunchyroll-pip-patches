# Gryphou's Morphe Patches

Morphe patch bundle for Crunchyroll and De'Longhi Comfort.

## About

This repository contains Morphe patches that:

- Enable Android Picture-in-Picture for Crunchyroll (`com.crunchyroll.crunchyroid`).
- Hide the `GetReceivedShares-003` incident popup, remove the login location requirement, and suppress notification permission and app review prompts in De'Longhi Comfort (`com.ddsx_ayla_android`).
- Restore the selected AC and reload its controls when returning to De'Longhi Comfort.

## Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.2.0-dev.1](https://github.com/NekoGryphou/gryphous-morphe-patches/releases/tag/v1.2.0-dev.1)**&nbsp;&nbsp;•&nbsp;&nbsp;`dev`&nbsp;&nbsp;•&nbsp;&nbsp;6 patches total
<details open>
<summary>📦 De'Longhi Comfort&nbsp;&nbsp;•&nbsp;&nbsp;5 patches</summary>
<br>

**🎯 Supported versions:**

| 5.1.5 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Disable login location requirement](#disable-login-location-requirement) | Allows login and registration without granting location permission. |  |
| [Disable notification permission prompt](#disable-notification-permission-prompt) | Prevents De'Longhi Comfort from asking to enable notifications. |  |
| [Disable review request dialog](#disable-review-request-dialog) | Prevents De'Longhi Comfort from asking to rate the app. |  |
| [Hide shared-devices incident popup](#hide-shared-devices-incident-popup) | Prevents the GetReceivedShares-003 incident popup from appearing. |  |
| [Restore AC controls on return](#restore-ac-controls-on-return) | Restores the selected air conditioner and reloads its controls when returning to De'Longhi Comfort. |  |

</details>

<details open>
<summary>📦 Crunchyroll&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 3.112.2 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Enable Picture-in-Picture](#enable-picture-in-picture) | Enables automatic Android Picture-in-Picture when leaving Crunchyroll playback. |  |

</details>

<!-- PATCHES_END -->

#### How to use these patches

Click here to add these patches to Morphe: https://morphe.software/add-source?github=NekoGryphou/gryphous-morphe-patches

Or manually add this repository url as a patch source in Morphe: https://github.com/NekoGryphou/gryphous-morphe-patches

### Building

To build Gryphou's Morphe Patches,
you can follow the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation).

## License

Gryphou's Patches are licensed under the [GNU General Public License v3.0](LICENSE)
