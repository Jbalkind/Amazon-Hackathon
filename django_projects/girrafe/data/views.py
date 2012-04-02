from django.http import HttpResponse
import twitter
import blogger
import youtube
import amazon

import json
import Sentiment, Document

sentiment = Sentiment.Sentiment()

def twitterJSON(request, query = ''):
	documents = []
	for data in getData(query, "twitter"):
		doc = Document.Document(data)
		documents += [doc]

	return HttpResponse(json.dumps(sentiment.analyse_list(documents)), content_type="application/json")

def bloggerJSON(request, query = ''):
	documents = []
	for data in getData(query, "blogger"):
		doc = Document.Document(data)
		documents += [doc]

	return HttpResponse(json.dumps(sentiment.analyse_list(documents)), content_type="application/json")


def youtubeJSON(request, query = ''):
	documents = []
	for data in getData(query, "youtube"):
		doc = Document.Document(data)
		documents += [doc]

	docsData = []
	for document in documents:
		docsData += [{'title': document.title, 'content': document.content, 'url': document.url, 'date': document.date, 'thumb': document.thumb}]		

	return HttpResponse(json.dumps({'videos': docsData}), content_type="application/json")


def amazonJSON(request, query = ''):
	return HttpResponse(json.dumps({'products': getData(query, "amazon")}), content_type="application/json")


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
	elif source == "amazon":
		return amazon.getAmazonContent(queryString)
	else:
		return "Error!"
