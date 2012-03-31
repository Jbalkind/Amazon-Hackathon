from config import AWS_KEY, SECRET_KEY
from jsonresults import JSONAPI as JsonApi
from amazonproduct.api import API
from amazonproduct.errors import AWSError
import xml.dom.minidom


def getText(nodelist):
    rc = []
    for node in nodelist:
        if node.nodeType == node.TEXT_NODE:
            rc.append(node.data)
    return ''.join(rc)

def minidom_response_parser(fp):
    root = xml.dom.minidom.parse(fp)
    # parse errors
    for error in root.getElementsByTagName('Error'):
        code = error.getElementsByTagName('Code')[0].firstChild.nodeValue
        msg = error.getElementsByTagName('Message')[0].firstChild.nodeValue
        raise AWSError(code, msg)
    return root

def getAmazonContent(query):

	api = API(AWS_KEY, SECRET_KEY, 'uk', associate_tag='giracake-21', processor=minidom_response_parser)
	node = api.item_search('All', Keywords=query, ResponseGroup="Medium")
	products = node.getElementsByTagName('Item')

	data = []

	for product in products:
		print product.toprettyxml()
		print
		print
		prod = {}
		prod['url'] = getText(product.getElementsByTagName('DetailPageURL')[0].childNodes)
		prod['title'] = getText(product.getElementsByTagName('Title')[0].childNodes)
		prod['desc'] = ''
		prod['img'] = getText(product.getElementByTagName(''))
		data += [prod]
	
	return data
print getAmazonContent('iPhone')