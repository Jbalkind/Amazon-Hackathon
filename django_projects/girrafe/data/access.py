import twitter
import blogger
import youtube

import json

from girrafe.data.sentiment import Sentiment, Document


def getData(queryString, source):

	queryString = queryString.replace(" ", "%20")

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

documents = []
for data in getData("amazon", "blogger"):
	doc = Document.Document(data)
	documents += [doc]

sentiment = Sentiment.Sentiment()
print json.dumps(sentiment.analyse_list(documents), sort_keys=True, indent=4)
