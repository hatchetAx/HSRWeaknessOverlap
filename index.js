const fs = require('fs')
const puppeteer = require('puppeteer-extra')
const StealthPlugin = require('puppeteer-extra-plugin-stealth')


enemies = [];
weakness = [];

const args = [
    '--no-sandbox',
    '--disable-setuid-sandbox',
    '--disable-infobars',
    '--window-position=0,0',
    '--ignore-certifcate-errors',
    '--ignore-certifcate-errors-spki-list',
    '--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36'
];

const options = {
    args,
    headless: "new",
    ignoreHTTPSErrors: true,
};



async function run() {

    puppeteer.use(StealthPlugin());

    const browser = await puppeteer.launch(options);


    const page = await browser.newPage();
    
    await page.goto('https://game8.co/games/Honkai-Star-Rail/archives/408174'); //insert url


    const x = await page.evaluate(() => {
        let enemies = [];
        document.querySelectorAll(".Enemy_cell.center").forEach(e => enemies.push(e.innerText.replaceAll(",", "")));
        return enemies;
    });

    const y = await page.evaluate(() => {
        let weakness = [];
        document.querySelectorAll(".Weakness_cell.left").forEach(w => weakness.push(w.innerText.trim().replaceAll("\n", "")))
        return weakness;
    });

    browser.close();

    enemies = x;

    weakness = y;

    fs.writeFile('enemies.txt', JSON.stringify(enemies), (err) => {
        if(err) throw err;
        console.log('enemies.txt saved');
    });
    
    fs.writeFile('weakness.txt', JSON.stringify(weakness), (err) => {
        if(err) throw err;
        console.log('weakness.txt saved');
    });

    console.log("done");

    

}

run();
