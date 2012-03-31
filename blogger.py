import urllib2
import simplejson
import re
from BeautifulSoup import BeautifulSoup as Soup
from soupselect import select
from threading import Thread, Lock

dataLock = Lock()
data = []
completed = 0

def visible(element):
    if element.parent.name in ['style', 'script', '[document]', 'head', 'title']:
        return False
    elif re.match('<!--.*-->', str(element)):
        return False
    return True



def getSearchURL(query):
	return ('https://ajax.googleapis.com/ajax/services/search/blogs?' +
	       'v=1.0&rsz=8&q=site:*.blogspot.com%20'+query)
	
def scrapeBlog(blog):
	global completed
	blogurl = blog['postUrl']
	blogData = {}
	try:
		soup = Soup(urllib2.urlopen(blogurl))
		post = select(soup, 'div.post-body')

		title = select(soup, 'h1.title')
		titleNoTags = Soup(str(title))
		rawTitle = ''.join(filter(visible, titleNoTags.findAll(text=True))).strip()
		#print rawTitle

		noScript = Soup(str(post))
		rawText = ''.join(filter(visible, noScript.findAll(text=True))).strip()
		#print raw_text
		
		blogData['source'] = str(rawTitle)
		blogData['title'] = blog['titleNoFormatting']
		blogData['content'] = str(rawText)
		blogData['date'] = blog['publishedDate']
		blogData['url'] = str(blogurl)
	except e:
		pass
	with dataLock:
		data.append(blogData)
		completed += 1
	
	

	
def getBloggerContent(query):
	
	global data
	
	url = getSearchURL(query)
	request = urllib2.Request(url, None, {'Referer': "noreferrer"})
	response = urllib2.urlopen(request)

	# Process the JSON string.
	results = simplejson.load(response)

	#print results
	
	threadPool = []
	
	threadCount = 0

	for blog in results['responseData']['results']:
		threadPool += [Thread(target=scrapeBlog, args=(blog,)).start()]
		threadCount += 1
	
	while(True):
		if (completed == threadCount):
			break
	for d in data:
		if d == {}:
			data.remove(d)

	return data
		

		#[s.extract() for s in noScript('script')]
		#print noScript

		#source = blog title
		#title = post title
		#content  = post
		#date
		#url

		#get 'postURL' entry for each post and scrape post content