import twitter
import blogger
import youtube
import amazon

def getData(queryString, source):

	if source == "twitter":
		#get twitter data
		return twitter.getTwitterContent(queryString)
	elif source == "youtube":
		#get YouTube data
		return youtube.getYoutubeContent(queryString)
	elif source == "blogger":
		#get blogger data
		return blogger.getBloggerContent(queryString)
	elif source == 'amazon'
		#get amazon data
		return amazon.getAmazonContent(queryString)
	else:
		return "Error: bad data source!"