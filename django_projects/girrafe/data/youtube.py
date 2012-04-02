import urllib2
import simplejson


def getSearchURL(query):
	return ('https://gdata.youtube.com/feeds/api/videos?max-results=10&v=2&alt=json&q='+query)
	
def getYoutubeContent(query):

	url = getSearchURL(query)
	request = urllib2.Request(url, None, {'Referer': "noreferrer"})
	response = urllib2.urlopen(request)

	# Process the JSON string.
	results = simplejson.load(response)
	data = []
	#print results['feed']['entry']
	for res in results['feed']['entry']:

		youtubeData = {}
		youtubeData['source'] = res['author'][0]['name']['$t']
		youtubeData['title'] = res['title']['$t']
		youtubeData['content'] = res['media$group']['media$description']['$t']
		youtubeData['date'] = res['published']['$t']
		youtubeData['url'] = res['content']['src']
		youtubeData['thumb'] = res['media$group']['media$thumbnail'][0]['url']
		print youtubeData
		print
		data += [youtubeData]
	
	return data

#youtubeData = {}
#youtubeData['source'] = str(rawTitle)
#youtubeData['title'] = blog['titleNoFormatting']
#youtubeData['content'] = str(rawText)
#youtubeData['date'] = blog['publishedDate']
#youtubeData['url'] = str(blogurl)
#youTubeData['thumb'] = 0
# now have some fun with the results...