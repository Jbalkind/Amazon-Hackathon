import Document

from math import sqrt
import sys

WORDLIST_SRC = "/home/ubuntu/django_projects/girrafe/sentiment_list.txt"

class Sentiment:
	
	def __init__(self):
		self.word_list = dict(map(lambda (w, s): (w, int(s)), [ws.strip().split('\t') for ws in open(WORDLIST_SRC)]))

	def analyse(self, document):
		total_score = 0;

		previous_word = "";
		for i in range(len(document.tokens)):
			word_score = self.word_list.get(document.tokens[i], 0)

			# Invert on double negatives. This list needs expanding
			if previous_word in ["not", "don't", "dont"]:
				word_score = word_score * -1
			elif previous_word in ["maybe", "kinda"]:
				word_score = word_score * 0.5

			total_score += word_score
			previous_word = document.tokens[i]
	
		if total_score != 0:
			return float(total_score) / sqrt(len(document.tokens))

		return 0.0 


	def analyse_list(self, doucment_list):
		if len(doucment_list) == 0:
			return 0.0

		overall_score = 0.0

		good_docs = []
		bad_docs = []
		neutral_docs = []
		for document in doucment_list:
			score = self.analyse(document)

			if score > 0.5:
				good_docs += [document]
			elif score < -0.5:
				bad_docs += [document]
			else:
				neutral_docs += [document]

			overall_score += score
			#print str(round(score,2)) + "\t" + document.content[:100]

		if overall_score != 0:
			overall_score = overall_score / sqrt(len(doucment_list))

		good_score = 0
		for document in good_docs:
			good_score += self.analyse(document)
		if good_score != 0:
			good_score = good_score / sqrt(len(good_docs))

		bad_score = 0
		for document in bad_docs:
			bad_score += self.analyse(document)
		if bad_score != 0:
			bad_score = bad_score / sqrt(len(bad_docs))

		docsData = []
		for document in doucment_list:
			docsData += [{'title': document.title, 'content': document.content, 'url': document.url, 'date': document.date}]		

		return {'sentiments': [len(good_docs), len(neutral_docs), len(bad_docs)],
			'good': good_score,
			'bad': bad_score,
			'docs': docsData
			}


