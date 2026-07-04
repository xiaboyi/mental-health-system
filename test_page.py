from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    page = browser.new_page()

    # Capture console errors
    errors = []
    page.on("console", lambda msg: errors.append(f"[{msg.type}] {msg.text}") if msg.type in ("error", "warning") else None)

    page.goto('http://localhost:3000')
    page.wait_for_load_state('networkidle')

    # Take screenshot
    page.screenshot(path='D:/DevTools/mental-health-system/screenshot.png', full_page=True)

    # Check for error messages on page
    try:
        el_message = page.locator('.el-message').all()
        for m in el_message:
            print(f"Page message: {m.text_content()}")
    except:
        pass

    print("\n=== Console Errors/Warnings ===")
    for e in errors:
        print(e)

    print("\n=== Page Info ===")
    print(f"URL: {page.url}")
    print(f"Title: {page.title()}")

    browser.close()
    print("\nScreenshot saved to screenshot.png")
