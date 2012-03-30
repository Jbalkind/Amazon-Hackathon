import twitter
import blogger
import youtube

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
	else:
		return "Error!"
