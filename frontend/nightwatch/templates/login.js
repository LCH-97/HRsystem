/**
 * To learn more about the describe interface, refer to the following link :
 * https://nightwatchjs.org/guide/writing-tests/test-syntax.html
 */
describe('The Login Page', function () {

  /**
   * This section will always execute before the test suite
   * Read More : https://nightwatchjs.org/guide/writing-tests/using-test-hooks.html#before-beforeeach-after-aftereach
   */
  // before(async function (browser) {
    /**
     * Navigate to a URL :
     *   - We need to navigate before performing any actions on the page
     *     Read More : https://nightwatchjs.org/api/navigateTo.html
     * 
     *   - <LOGIN-PAGE-URL> is a placeholder. Replace it with the actual URL, which you wanted to navigate and login
     * 
     * Eg: browser.navigateTo('https://the-internet.herokuapp.com/login');
     */

    
  // });

  
  /* The following will perform the actual test/assertions */
  it('To check successful login', async function(browser) {
  
    browser.navigateTo('http://192.168.0.51');

    /**
     * Check if input box with id 'username' is present :
     *   - Use elementPresent command, to check if the input box is present or not.
     *     Read More : https://nightwatchjs.org/api/assert/#assert-elementPresent
     *   
     *   - Pass in the css locator to check if the input box (username) is present.
     *     Read More: https://www.selenium.dev/documentation/webdriver/elements/locators/
     * 
     * Eg: browser.assert.elementPresent('input[id=username]');
     */

    browser.assert.elementPresent('#inputEmail');


    /**
    * Enter the USERNAME :
    *   - Use sendKeys command to fill the username input.
    *     Read More : https://nightwatchjs.org/api/sendKeys.html
    * 
    *   - <USERNAME> is a placeholder. Replace it with the actual username
    *   - <css locator> will be same as above in elementPresent
    * 
    * Eg: browser.sendKeys('input[id=username]', 'tomsmith');
    */

    browser.sendKeys('#inputEmail', 'admin');


    /**
     * Check if input box with id 'password' is present :
     *   - Pass in the css locator to check if the input box (password) is present.
     * 
     * Eg: browser.assert.elementPresent('input[id=password]');
     */

    browser.assert.elementPresent('#inputPassword');


    /**
      * Enter the Password and form submission :
      *   - browser.Keys.ENTER is used to press ENTER keystroke after filling the password, to submit the form. 
      *   - <PASSWORD> is a placeholder. Replace it with the actual password
      *   - <css locator> will be same as above passed for password in elementPresent command
      * 
      * Eg: browser.sendKeys('input[id=password]', ['SuperSecretPassword!', browser.Keys.ENTER]);
      */

    browser.sendKeys('#inputPassword', 'qwer1234');


    /**
     * If ENTER doesn't work for you then you can use 'click' command to click on submit button.
     * Read More : https://nightwatchjs.org/api/click.html
     * 
     * Eg: browser.click('css selector', 'button[type="submit"]');
     */
    browser.click('.btn-primary');
    await browser.pause(1000);
    /**
     * Check the new URL is correct :
     *   - Use urlContains command to check the URL of the current page.
     *     Read More : https://nightwatchjs.org/api/assert/#assert-urlMatches
     * 
     *   - <NEW-REDIRECTED-URL> is a placeholder. Replace it with the actual dashboard/profile URL
     * 
     * Eg: browser.assert.urlContains('/secure');
     */

    browser.assert.urlContains('http://192.168.0.51/');


    /**
     * Check session cookie is present after successful login.
     * Read More: https://nightwatchjs.org/api/getCookie.html
     * 
     * Eg: browser.getCookie('rack.session', function callback(result) {
     *       this.assert.equal(result.name, 'rack.session');
     *       this.assert.equal(result.value.length, 532);
     *     });
     */
    // browser.execute(() => {
    //   const value = sessionStorage.getItem('token');
    //   expect(value).not.toBeNull();
    
    // });
    
    
    browser.end();
  });


  /* The following will always execute after the test suite */
  // after(function (browser) {
    // This is used to close the browser's session
    
  // });
});
