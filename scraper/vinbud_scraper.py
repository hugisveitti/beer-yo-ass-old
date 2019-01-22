from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from bs4 import BeautifulSoup
import re
import pandas as pd
import os
import json

from time import sleep


url = "https://www.vinbudin.is/heim/vorur/vorur.aspx/?category=beer&order=price%20desc"
# create a new Firefox session
# verðið að ná í driverinn https://github.com/mozilla/geckodriver/releases
driver = webdriver.Firefox()
driver.implicitly_wait(30)
driver.get(url)

next_button = driver.find_elements_by_class_name("next")
allBeers = []
for i in range(20):
    soup=BeautifulSoup(driver.page_source, 'lxml')
    for product in soup.find_all('li', class_="product"):
        title_a = product.find('a', class_="title")
        title = title_a.find_all('span')[0].text
        price = product.find('span', class_="price").text
        volume = product.find('span', class_="volume").text
        alcohol = product.find('span', class_="alcohol").text
        product_number = product.find('span', class_="product-number").text
        #það eru svigar utan um product number
        product_number = product_number[1:len(product_number)-1]

        text = product.find('span', class_="text")

        # print(product)
        # print(price)
        # print(volume)
        # print(alcohol)
        # print(product_number)
        #
        # # text er einhversskonar tegund af bjór og ekki allt er með þetta
        # print(text)
        #
        # print()

        beer = {
        "title": title,
        "price":price,
        "volume": volume,
        "alcohol": alcohol,
        "product_number": product_number
        }

        allBeers.append(beer)
        # print(beer)

    print(next_button)
    next_button[0].click()
    #fer eftir internet tengingu hvad tharf ad sleepa lengi
    sleep(0.1)


with open('data.json', 'w') as outfile:
    json.dump(allBeers, outfile,ensure_ascii=False)


driver.quit()
