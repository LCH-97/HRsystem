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
    it('To check successful sidebar action', async function(browser) {
    
      browser.navigateTo('http://192.168.0.51');
  
      
  
      browser.assert.elementPresent('#inputEmail');  
      browser.sendKeys('#inputEmail', 'admin');
      browser.assert.elementPresent('#inputPassword');
      browser.sendKeys('#inputPassword', 'qwer1234');
     
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
      browser.click('.btn-primary');
      await browser.pause(1000);
      browser.assert.urlContains('http://192.168.0.51/main');
  
      browser.click('#GooutMenu');
      await browser.pause(1000);
      browser.click('#GooutCreate');
      await browser.pause(1000);
      browser.assert.urlContains('http://192.168.0.51/goout/create');
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
  