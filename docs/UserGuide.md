---
layout: page
title: KnotBook User Guide
pageNav: 3
---

KnotBook is a desktop app designed exclusively for **wedding planners** to manage client and vendor contacts, keeping track of every wedding and connection you coordinate throughout your planning journey. As a wedding planner, you can add new clients and vendors to the app via a simple to use **Command Line Interface (CLI)**. Furthermore, you can link vendors to clients, edit contact details, and organize everything in one centralized place. You will never lose track of which florist is working on which wedding or miss important client details after using KnotBook!

**Figure 1: Illustration of KnotBook’s Client-Vendor Management Feature**
![LinkedUI.png](images/LinkedUI.png)


## Quick Navigation

1. [Quick start](#quick-start)
   - [Step 1: Install Java](#step-1-install-java)
   - [Step 2: Download KnotBook](#step-2-download-knotbook)
   - [Step 3: Run KnotBook](#step-3-run-knotbook)
   - [Step 4: Start Using KnotBook](#step-4-start-using-knotbook)
   - [Step 5: Understanding Your Data](#step-5-understanding-your-data)
2. [Command summary](#command-summary)
3. [Features](#features)
   - [Viewing help](#viewing-help--help-)
   - [Parameter reference](#parameter-reference)
   - [Adding a contact](#adding-a-contact-add-)
   - [Listing all contacts](#listing-all-contacts--list-)
   - [Editing a contact](#editing-a-contact--edit-)
   - [Finding contacts by name](#finding-contacts-by-name-find-)
   - [Filtering by category](#filtering-by-category--cat-)
   - [Linking a vendor to a client](#linking-a-vendor-to-a-client--link-)
   - [Unlinking a vendor from a client](#unlinking-a-vendor-from-a-client--unlink-)
   - [Deleting a contact](#deleting-a-contact--delete-)
   - [Clearing all entries](#clearing-all-entries--clear-)
   - [Exiting the program](#exiting-the-program--exit-)
   - [Saving the data](#saving-the-data)
   - [Editing the data file](#editing-the-data-file)
4. [FAQ](#faq)
5. [Known issues](#known-issues)

--------------------------------------------------------------------------------------------------------------------
## Quick start

### Step 1: Install Java
KnotBook needs Java 17 or newer to run. Think of Java as the engine that powers the app.

1. **Check if you already have Java:**
    - **Windows:** Open Command Prompt (search "cmd" in Start menu), type `java -version` and press Enter.
    - **Mac:** Open Terminal (search "Terminal" in Spotlight) (open Spotlight by simply pressing Command + Spacebar), type `java -version` and press Enter.
    - If you see "java version 17" or higher, you're good to go! Skip to Step 2.
    - If you see an error or a lower version, continue below.

2. **Install Java 17:**
    - **Windows/Linux:** Download from [Oracle](https://www.oracle.com/java/technologies/downloads/#java17)
    - **Mac:** Follow [this guide](https://se-education.org/guides/tutorials/javaInstallationMac.html) for the exact version needed

### Step 2: Download KnotBook
1. Download the latest `KnotBook.jar` file from [our releases page](https://github.com/AY2526S1-CS2103T-T16-3/tp/releases): scroll down until you see the latest "Assets" section, then simply click on `KnotBook.jar` to download it
2. Create a new folder on your computer where you want to keep KnotBook and all its data (e.g., `Documents/KnotBook`)
3. Move the downloaded `KnotBook.jar` file into this folder

### Step 3: Run KnotBook
**Easy way (Double-click):**
- Simply double-click the `KnotBook.jar` file
- If this doesn't work, try the command line method below

**Command line way:**
1. Open your command terminal:
    - **Windows:** Search for "Command Prompt" or "PowerShell" in the Start menu
    - **Mac:** Search for "Terminal" in Spotlight (Cmd + Space)
2. Navigate to your KnotBook folder:
    - Type `cd ` (with a space after cd)
    - Drag and drop your KnotBook folder into the terminal window
    - Press Enter
3. Run KnotBook by typing: `java -jar KnotBook.jar` and press Enter

### Step 4: Start Using KnotBook
The app window should appear in a few seconds with some sample wedding contacts to help you get started.

**Figure 2: Main window**
![AnnotatedUi.png](images/AnnotatedUi.png)

**Legend - numbered callouts in Figure 2**
1. **Contact Details Panel**: The panel on the right shows the full details of the selected contact. Click any contact in the list to view their information here - including linked vendors/clients when available.
2. **Command Result Display**: This area displays feedback for every command you run (e.g., success messages, errors, and helpful hints). Use it to confirm what changed after each action.
3. **Input Command Box**: Type your commands here (like `add`, `edit`, `find`, `cat`, `link`, `unlink`, `delete`). Press Enter to run the command.
4. **Contact List Panel**: This area displays all your contacts currently shown in KnotBook - including both clients and vendors. The list automatically updates after commands like `add`, `edit`, `delete`, `find`, `cat`, `list`, or `clear`.
5. <span id="contact-index"></span>**Contact index (`INDEX`)**: The numbers on the left of the contact card are the indices you use in commands such as `edit INDEX`, `delete INDEX`, `link client/CLIENT_INDEX vendor/VENDOR_INDEX`, and `unlink ...`. Indices always refer to the currently displayed list (even when the currently displayed list changes after a `find` or `cat` command).

**Try these commands** by typing them in the command box at the bottom right and pressing Enter:
* `help` - Opens a help window showing all available commands
* `list` - Shows all your contacts (clients and vendors)
* `cat florist` - Shows only vendors categorized as florist
* `add n/Jane Smith p/91234567 e/jane@flowers.com a/123 Orchard Road type/vendor price/1500 c/florist` - Adds a new florist vendor
* `add n/John Doe p/98765432 e/johnd@example.com a/311 Clementi Ave type/client w/15-06-2026 pr/Jane Doe budget/9000-10000` - Adds a new client couple
* `delete 3` - Deletes the 3rd contact in the current list
* `exit` - Closes the app

**Need more help?** Type `help` and press Enter to see detailed instructions for all commands!

### Step 5: Understanding Your Data
- All your contact data is automatically saved in a file called `addressbook.json` in the `data` folder next to your `KnotBook.jar`
- You don't need to manually save - KnotBook saves automatically after every change
- To backup your data, just copy the entire `data` folder to a safe location

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
IMPORTANT: Take note that you should only use our app to edit your data and never edit the JSON file in your directory directly.
</div>

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add Client** | `add n/NAME p/PHONE e/EMAIL a/ADDRESS type/client w/WEDDING_DATE pr/PARTNER [budget/BUDGET]​` <br> e.g., `add n/John Doe p/98765432 e/johnd@example.com a/311 Clementi Ave type/client w/15-06-2026 pr/Jane Doe budget/10000`
**Add Vendor** | `add n/NAME p/PHONE e/EMAIL a/ADDRESS type/vendor [price/PRICE] [c/CATEGORY]​` <br> e.g., `add n/Blooming Flowers p/91234567 e/contact@blooming.com a/123 Orchard Rd type/vendor price/1500 c/florist`
**List All Contacts** | `list`
**Find a Contact** | `find <keyword>`<br> e.g., `find Ch`
**Filter by Category** | `cat CATEGORY`<br> e.g., `cat florist`
**Edit Contact** | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [w/WEDDING_DATE] [pr/PARTNER] [price/PRICE] [budget/BUDGET] [c/CATEGORY]​` (see [INDEX](#contact-index))<br> e.g., `edit 2 p/91234567 budget/8000`
**Delete Contact** | `delete INDEX`<br> e.g., `delete 3`
**Link Client with Vendor** | `link client/CLIENT_INDEX vendor/VENDOR_INDEX`<br> e.g., `link client/1 vendor/3`
**Unlink Client with Vendor** | `unlink client/CLIENT_INDEX vendor/VENDOR_INDEX`<br> e.g., `unlink client/1 vendor/3`
**Clear** | `clear` - Deletes all contacts
**Help** | `help` - Opens help window
**Exit** | `exit` - Closes the application

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Replace words in `UPPER_CASE` with your own details. For example, in `add n/NAME`, you might type `add n/Jane Ong`.

* Items in square brackets `[ ]` are optional. For example, `n/NAME [c/CATEGORY]` requires a name; the category can be omitted.

* Commands are case-sensitive - watch your capitals.

* You can enter parameters in any order. For example, `link client/1 vendor/2` works the same as `link vendor/2 client/1`.

* For very simple commands like `help`, `list`, `exit` and `clear`, any extra text is politely ignored.

* Each command must be entered on a single line. If you copy something that looks like several lines, simply join them before pressing Enter.

* Dates in the app appear as `YYYY-MM-DD`, though when typing a date you may use either `DD-MM-YYYY` or `YYYY-MM-DD`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

<div markdown="block" class="alert alert-warning">
:exclamation: **Warning:**  
Some commands permanently change or delete data. Read carefully before running:

* `delete INDEX` (see [INDEX](#contact-index)): Deletes a contact and can’t be undone.
* `clear`: Deletes all contacts, not just what’s filtered; saves immediately.
* `edit`: Changes take effect right away; `c/` alone removes all categories.
* `add`: You can’t change `type/TYPE` later with `edit` (you have to delete and re-add).
</div>

### Viewing help : `help` 💡

Opens a help window with detailed information about all available commands in an easy-to-navigate accordion interface.

**Figure 3: Help window**
![help.png](images/help.png)

Format: `help`

**Tip:** Click on any command name in the help window to expand and see detailed usage instructions and examples.

### Parameter reference

* `n/NAME` - Contact name (required)
    * Can contain alphanumeric characters, spaces, and special characters like apostrophes (`'`), hyphens (`-`), periods (`.`), and ampersands (`&`)
    * Must start with an alphanumeric character
    * Example: `John Chia`, `O'Brien Catering`, `Bloom & Co.`
    * Example: `John Chia`, `O'Brien Catering`, `Bloom & Co.`
* `p/PHONE` - Phone number (required)
    * Singapore context: must be exactly 8 digits (no `+65`, spaces, or symbols)
    * No letters, spaces, hyphens, or other special characters allowed
    * Example: `91234567`, `98765432`
* `e/EMAIL` - Email address (required)
    * Must follow standard internet email format (specifically RFC 5322 standard), meaning:
        * Must contain exactly one `@` symbol
        * Must have a valid domain (e.g., `.com`, `.net`, `.sg`)
        * No spaces
    * Example: `john@example.com`, `contact.us@blooming-flowers.sg`
    * *Technical details:* KnotBook uses [Apache Commons Validator EmailValidator](https://commons.apache.org/proper/commons-validator/apidocs/org/apache/commons/validator/routines/EmailValidator.html)
* `a/ADDRESS` - Physical address (required)
    * Can contain any characters
    * For clients, this can be the wedding venue or a general location if you prefer not to store personal home addresses
    * Example: `123 Orchard Road, #05-01`, `Grand Hyatt Singapore`, `Singapore (TBD venue)`
* `type/TYPE` - Either `client` or `vendor` (required, case-insensitive)
* `w/WEDDING_DATE` - Wedding date (required, for clients only)
    * Accepts formats: `DD-MM-YYYY` or `YYYY-MM-DD`
    * Must be a valid, real date whereby
        * `DD` ranges from `01` to either `28` or `29` for February (depending on leap years), and to `30` or `31` (depending on the month) for other months
        * `MM` ranges from `01` to `12`
        * `YYYY` ranges from `0000` to `9999` (only four-digit years supported)
    * Example: `15-06-2026` or `2026-06-15`
* `pr/PARTNER` - Partner name (required, for clients only)
    * Can contain alphanumeric characters, spaces, and special characters like apostrophes (`'`), hyphens (`-`), periods (`.`), and ampersands (`&`)
    * Must start with an alphanumeric character
    * Example: `Jane Wang`, `O'Brien`
* `price/PRICE` - Vendor pricing (optional, for vendors only)
    * Must be a non-negative integer (whole numbers only, no cents/decimals, no commas, no fullstops, and leading zeros will be trimmed)
    * Can be a single value (e.g., `1000`) or range (e.g., `1000-2000`)
    * Range values must be separated by a hyphen with no spaces
    * Maximum value: 9,999,999,999
* `budget/BUDGET` - Client budget (optional, for clients only)
    * Must be a non-negative integer (whole numbers only, no cents/decimals, no commas, no fullstops, leading zeros will be trimmed)
    * Can be a single value (e.g., `5000`) or range (e.g., `5000-10000`)
    * Range values must be separated by a hyphen with no spaces
    * Maximum value: 9,999,999,999
* `c/CATEGORY` - Category tag (optional, for vendors only)
    * Each vendor can have only one category
    * Must start with an alphanumeric character
    * Can contain spaces, hyphens (`-`), ampersands (`&`), periods (`.`), apostrophes (`'`), slashes (`/`), and parentheses (`()`)
    * Limit of 30 characters and a minimum of 2 characters
    * Example: `makeup artist`, `photographer`

<div markdown="block" class="alert alert-primary">

:bulb: **Used by these commands:**

* **add, edit**: Use fields as defined in the [Parameter reference](#parameter-reference).
* **cat**: Category matches follow `c/CATEGORY` rules.
* **find**: Matches `NAME` and client `PARTNER` (see `n/NAME`, `pr/PARTNER`).

</div>

### Adding a contact: `add` 👯‍♀️

* Clients: When a new couple signs with you, use this to create their client profile in KnotBook!
* Vendors: When you onboard a vendor (e.g., photographer, florist, venue), use this command to add their vendor profile!

**Format for adding a client:**
`add n/NAME p/PHONE e/EMAIL a/ADDRESS type/client w/WEDDING_DATE pr/PARTNER [budget/BUDGET]​`

**Format for adding a vendor:**
`add n/NAME p/PHONE e/EMAIL a/ADDRESS type/vendor [price/PRICE] [c/CATEGORY]​`

**Parameters:** See [Parameter reference](#parameter-reference)

**Note:**
* The name of the contact is case-sensitive so Blooming Bouquets and BLOOMING Bouquets are treated as 2 different contacts; Be careful in inputting names!
* When you type in an `add` command after a `find` command, the displayed list will reset from the filtered results.

**Duplicate contacts:**
* If a new contact has a phone that already exists in KnotBook, the add will be rejected. This is because we assume each person has a unique phone number.
* Clients and vendors share the same pool. You can’t have a client and a vendor with the same phone number.

<div markdown="block" class="alert alert-primary">

:bulb: **Tips:**

* For **clients** (wedding couples), wedding date and partner name are required. You can also include their budget.
* For **vendors** (service providers), you may include pricing and use categories to classify them (e.g., `florist`, `makeup artist`, `photographer`).

</div>

**Examples:**
* **Adding a client:**<br>
  `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 type/client w/15-06-2026 pr/Jane Doe budget/5000-10000`

* **Adding a vendor:**<br>
  `add n/Blooming Flowers p/91234567 e/contact@blooming.com a/123 Orchard Road type/vendor price/1000-2000 c/florist`

### Listing all contacts : `list` 📜

Shows a list of all contacts (both clients and vendors) in KnotBook.

Format: `list`

**Example:**
* `list` - Displays all your wedding contacts

**Figure 4: List view**
![list.png](images/list.png)
Note: Dates shown in the contacts list are formatted as `YYYY-MM-DD` (e.g., `2026-07-15`).

### Editing a contact : `edit` ✍️

Made a typo? Client changed their wedding date? Vendor updated their pricing? Use this command to quickly update any contact details in KnotBook!

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [w/WEDDING_DATE] [pr/PARTNER] [price/PRICE] [budget/BUDGET] [c/CATEGORY]​` (see [INDEX](#contact-index))

See [Parameter reference](#parameter-reference) for detailed field rules.

**How it works:**
* Edits the contact at the specified `INDEX` (the number shown in the contact list)
* The index **must be a positive integer** (1, 2, 3, …) and **must not exceed the total number of contacts** in the displayed list
* **Categories can only be edited for vendors** (not clients). Attempting to add, remove, or modify categories on a client will result in an error. 
* To remove a category, type `c/` without specifying any categories after it
* At least one optional field must be provided
* When you type in an `edit` command after a `find` or `cat` command, you can only edit from the filtered contacts.

**Examples:**
* `edit 1 p/91234567 e/newemail@example.com` - Updates the phone and email of the 1st contact
* `edit 2 n/Blooming Flowers Studio c/florist` - Updates the name and category of the 2nd contact
* `edit 3 price/1500-2500` - Updates the pricing for a vendor
* `edit 4 budget/8000` - Updates the budget for a client

<div markdown="block" class="alert alert-warning">

:exclamation: **Warning:**  
Existing values will be replaced with the new input values.

</div>

### Finding contacts by name: `find` 🔍

Looking for a specific client or vendor? Quickly search by name - and for clients, the search also checks the partner’s name - with this handy command!

Format: `find <keyword>`

See [Parameter reference](#parameter-reference) for name and partner field rules.

**How it works:**
* The search is case-insensitive (e.g., `hans` matches `Hans`).
* Your entire input is treated as one query; spaces are not split into separate keywords (e.g., `roy b` is treated as `"roy b"`).
* Only the name field is searched; for clients, the partner’s name is also checked.
* Matches the start of any word (prefix match). For example, `Han` matches `Hans`, but `an` does not match `Hans`.
* **Valid characters:** Letters (a-z, A-Z), numbers (0-9), spaces, apostrophes (`'`), periods (`.`), ampersands (`&`), and hyphens (`-`). Invalid characters (such as brackets, parentheses, or special symbols) are not allowed.

**Examples:**
* `find tan` - Returns contacts like `Alex Tan`, `Tan Wei Ling`; also matches a client whose partner is Tan Jun Hao.
* `find ch` - Returns `Charlotte K Photography`; also matches a client whose partner is `Jack Chia`.<br>
  **Figure 5: Find results for `find ch`**
  ![find.png](images/find.png)
* `find &` - Returns contacts whose names start with `&`, such as `Ben & Jerry`.

<div markdown="block" class="alert alert-primary">

:bulb: **Tips:**

* After using `find`, run `list` to show all contacts again.
* `link`/`unlink` use the currently displayed list - if unsure, `list` first.

</div>


### Filtering by category : `cat`  🏷️

Need to find a photographer for an upcoming wedding? Want to see all your caterers at once? Filter contacts by category to instantly view all vendors of a specific type!

Format: `cat CATEGORY`

See [Parameter reference](#parameter-reference) for category rules.

**How it works:**
* Shows only vendors with categories matching the specified category exactly
* The search requires the full category name (e.g., `florist` will not match `fl` or `flor`)
* The search is case-insensitive (e.g., `florist` matches `Florist`)
* Useful for quickly viewing all vendors of a specific type

**Examples:**
* `cat florist` - Shows all vendors categorized as florist
* `cat photographer` - Shows all photographers
* `cat caterer` - Shows all catering services

  **Figure 6: Category results**
  ![cat.png](images/cat.png)

<div markdown="block" class="alert alert-primary">

:bulb: **Tips:**

* **Only vendors** have categories - clients won’t show up in `cat` results.
* Category matching is **case-insensitive**.
* In a filtered view (after `cat` or `find`), commands like `delete INDEX` use the [index](#contact-index) from the *filtered list*.
* To reset the view, run `list`.

</div>

### Linking a vendor to a client : `link` 🔗

Hired a photographer for the Johnson wedding? Use this command to connect vendors with their clients, so you can easily track which vendors are working on which weddings!

Format: `link client/CLIENT_INDEX vendor/VENDOR_INDEX`

**How it works:**
* Running the `link` command links the client at `CLIENT_INDEX` with the vendor at `VENDOR_INDEX`
  * `CLIENT_INDEX` is the [index](#contact-index) of a **client** contact in the currently displayed list
  * `VENDOR_INDEX` is the index of a **vendor** contact in the currently displayed list
* Both indices **must be a positive integer** (1, 2, 3, …) and **must not exceed the total number of contacts** in the displayed list
* The contact at `CLIENT_INDEX` must be a client, and the contact at `VENDOR_INDEX` must be a vendor
* Helps you track which vendors are assigned to which weddings

**Examples:**
* `link client/2 vendor/3` - Links the 2nd contact (who is a client) with the 3rd contact (who is a vendor)

**Figure 7: Linking example (view of 2nd contact which is of client type)**
![link1.png](images/link1.png)
**Figure 8: Linking example (view of 3rd contact which is of vendor type)**
![link2.png](images/link2.png)

### Unlinking a vendor from a client : `unlink` ⛓️‍💥

Client switched photographers? Vendor cancelled? Use this command to remove the connection between a client and vendor when plans change.

Format: `unlink client/CLIENT_INDEX vendor/VENDOR_INDEX` (uses [INDEX](#contact-index))

**How it works:**
* Removes the link between the client at `CLIENT_INDEX` and the vendor at `VENDOR_INDEX`
* Useful when a vendor is changed or a service is cancelled

**Examples:**
* `unlink client/1 vendor/3` - Removes the link between the client at index 1 and vendor at index 3
* `unlink client/2 vendor/5` - Removes the link between the client at index 2 and vendor at index 5

<div markdown="block" class="alert alert-warning">

:exclamation: **Warning:**  
Both `CLIENT_INDEX` and `VENDOR_INDEX` refer to the **currently displayed** list (including after `find` or `cat`).  
Ensure the chosen indices point to a **client** and a **vendor** respectively, or the command will fail.

</div>

<div markdown="block" class="alert alert-primary">

:bulb: **Tips for link/unlink:**

* After `find` or `cat`, run `list` if you’re unsure about indices.
* You can pick indices from any filtered view (e.g., `find tan` then `link client/1 vendor/2`) - the indices come from that filtered list.
* **link:** If the client and vendor are already linked, you’ll see an “already linked” message.
* **unlink:** If the pair isn’t currently linked, you’ll see a “not linked” message.
* Both commands update *both* records (the client’s linked vendors and the vendor’s linked clients stay in sync).

</div>

### Deleting a contact : `delete` 🗑️

Need to remove a contact? Whether a vendor is no longer available or a client cancelled, use this command to delete any contact from KnotBook.

Format: `delete INDEX` (see [INDEX](#contact-index))

**How it works:**
* Deletes the contact at the specified `INDEX`
* The index refers to the number shown in the currently displayed contact list
* The index **must be a positive integer** (1, 2, 3, …) consisting of **digits only** and **must not exceed the total number of contacts** in the displayed list

<div markdown="block" class="alert alert-warning">

:exclamation: **Warning:**  
This action cannot be undone! Make sure you're deleting the right contact.

</div>

<div markdown="block" class="alert alert-primary">

:bulb: **Tips:**
* Indices refer to the *currently displayed* list (after `find`, `cat`, etc.).
* If you're unsure about indices, run `list` to reset the view before deleting.

</div>

**Examples:**
* `list` followed by `delete 2` - Deletes the 2nd contact in the full list
* `find Blooming` followed by `delete 1` - Deletes the 1st contact in the search results
* `cat florist` followed by `delete 3` - Deletes the 3rd contact in the filtered list

### Clearing all entries : `clear` 🧹

Need a clean slate? This command deletes ALL contacts from KnotBook at once.

Format: `clear`

<div markdown="span" class="alert alert-warning">:exclamation: **Warning:**
This will delete ALL your contacts permanently! Make sure to back up your data first.
</div>

### Exiting the program : `exit` 👋

All done for the day? Use this command to close KnotBook!

Format: `exit`

### Saving the data

Your data is saved automatically to your hard disk after every change - no manual saving needed.

### Editing the data file

KnotBook data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Use the app to make changes; editing the JSON directly may corrupt your data.

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install KnotBook on the other computer, then copy the `data` folder from your old computer to the new KnotBook folder, replacing the new `data` folder.

**Q**: What's the difference between a client and a vendor?<br>
**A**: A **client** is a couple planning their wedding (your customers). A **vendor** is a service provider like a florist, caterer, or photographer.

**Q**: How do I see which vendors are linked to a specific client?<br>
**A**: Click on a client contact in the list to view their details, which will show all linked vendors.

**Q**: Can I use KnotBook offline?<br>
**A**: Yes! KnotBook works completely offline and doesn't require an internet connection.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

