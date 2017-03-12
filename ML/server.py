from bottle import route, request, run
from textblob import TextBlob
from lxml import html
import requests
import unicodedata
from keras.models import load_model
from PIL import Image, ImageOps
from keras.preprocessing import image
import numpy as np


model = load_model('total_top.h5')


@route('/test', method='GET')
def test():
    """Test function for REST service."""
    return {"message": "Funciona!!!!!!!!!!"}


def web_scrapping(web_id):

    page = requests.get('http://www.adidas.es/' + web_id + '.html')
    tree = html.fromstring(page.content)
    reviews = tree.xpath('//span[@itemprop="description"]/text()')
    sentiment = 0
    for rev in reviews:
        try:
            tr = u''.join(c for c in unicodedata.normalize('NFD', rev)
                          if unicodedata.category(c) != 'Mn')
        except TypeError as e:
            tr = rev

        testimonial = TextBlob(tr)
        testimonial = testimonial.translate(to='en')
        sentiment += testimonial.sentiment[1]

    sentiment /= len(reviews)
    sentiment = (sentiment / 2) + 0.5

    price = tree.xpath('//span[@class="sale-price"]')[0]
    price = price.attrib['data-sale-price']

    rating = tree.xpath('//span[@class="bvseo-ratingValue"]/text()')[0]

    product_name = tree.xpath('//h1[@class="title-32 vmargin8"]/text()')[0]

    url = tree.xpath('//img[@class="productimagezoomable"]')[0]
    url = url.attrib['src']

    return sentiment, price, rating, product_name, url


@route('/upload', method='POST')
def upload():
    imgdata = request.files.get('picture')
    imgdata.save('img_uploaded.jpg', overwrite=True)
  
    im = Image.open("img_uploaded.jpg")
    thumb = ImageOps.fit(im, (250,250), Image.ANTIALIAS)
    thumb = image.img_to_array(thumb) / 255.0
    
    pred = model.predict(np.array([thumb]))
    pred = np.argmax(pred, axis=1)

    hardcodeo = {
        0: 'BB5476',
        1: 'S76777',
        2: 'AQ1865',
        3: 'C77124',
        4: 'BA8143',
        5: 'M20325'
    }

    print pred
    web_id = hardcodeo[pred[0]]

    sentiment, price, rating, modelo, url = web_scrapping(web_id)


    try:
        return {
            "image": "OK",
            "modelo": modelo,
            "precio": price,
            "rating": rating,
            "sentiment": sentiment,
            "url": url
        }
    except Exception as e:
        return {"error": str(e)}


def main():
    """Main method."""
    run(host='0.0.0.0', port=8080)


if __name__ == "__main__":
    main()
