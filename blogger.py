import urllib2
import simplejson
import re
from BeautifulSoup import BeautifulSoup as Soup
from soupselect import select


def visible(element):
    if element.parent.name in ['style', 'script', '[document]', 'head', 'title']:
        return False
    elif re.match('<!--.*-->', str(element)):
        return False
    return True



def getSearchURL(query):
	return ('https://ajax.googleapis.com/ajax/services/search/blogs?' +
	       'v=1.0&rsz=8&q=site:*.blogspot.com%20'+query)
	
	
def getBloggerContent(query):
	
	url = getSearchURL(query)
	request = urllib2.Request(url, None, {'Referer': "noreferrer"})
	response = urllib2.urlopen(request)

	# Process the JSON string.
	results = simplejson.load(response)

	#print results
	
	data = []

	for blog in results['responseData']['results']:
		blogurl = blog['postUrl']
		#print blog

		soup = Soup(urllib2.urlopen(blogurl))
		post = select(soup, 'div.post-body')
		
		title = select(soup, 'h1.title')
		titleNoTags = Soup(str(title))
		rawTitle = ''.join(filter(visible, titleNoTags.findAll(text=True))).strip()
		#print rawTitle
		
		noScript = Soup(str(post))
		rawText = ''.join(filter(visible, noScript.findAll(text=True))).strip()
		#print raw_text
		
		blogData = {}
		blogData['source'] = str(rawTitle)
		blogData['title'] = blog['titleNoFormatting']
		blogData['content'] = str(rawText)
		blogData['date'] = blog['publishedDate']
		blogData['url'] = str(blogurl)
		#print blogData
		data += [blogData]
	return data
		

		#[s.extract() for s in noScript('script')]
		#print noScript

		#source = blog title
		#title = post title
		#content  = post
		#date
		#url

		#get 'postURL' entry for each post and scrape post content