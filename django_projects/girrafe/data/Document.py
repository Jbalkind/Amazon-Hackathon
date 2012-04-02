import string
import re

STRING = 1

class Document:

	def __init__(self, data):
		self.title = data.get('title', '')
		self.content = data.get('content', '').strip()
		self.url = data.get('url', '')
		self.date = data.get('date', '')
		self.thumb = data.get('thumb', '')

		self.content = self.content.replace("\n", " ")

		self.tokens = []
		for token in re.findall(r"[a-zA-Z']+", self.content):
			if len(token) >= 3:
				self.tokens += [string.lower(token)]

	def __str__(self):
		return self.content
