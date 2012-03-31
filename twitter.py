import urllib2
import simplejson as json

def getSearchURL(query):
	return ('http://search.twitter.com/search.json?q='+query)

def getTwitterContent(query):

	url = getSearchURL('query')
	request = urllib2.Request(url, None)
	response = urllib2.urlopen(request)

	# Process the JSON string.
	results = json.load(response)
	tweets = results['results']
	data = []

	for tweet in tweets:
		#print tweet
		#print tweet['text'], tweet['created_at']

		tweetData = {}
		tweetData['source'] = tweet['from_user']
		tweetData['title'] = tweet['text']
		tweetData['content'] = tweet['text']
		tweetData['date'] = tweet['created_at']
		tweetData['url'] = 'http://twitter.com/#!/' + tweet['from_user'] + '/status/' + tweet['id_str']
		tweetData['thumb'] = tweet['profile_image_url']
	
		data += [tweetData]
	
	return data

#print res
#print results['results']

# now have some fun with the results...