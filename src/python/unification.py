#!/usr/bin/env python2.7
# -*- coding: utf-8 -*-

'''
unification experiments
'''

import utils
import re
import string
from collections import defaultdict

# from http://hetland.org/coding/python/levenshtein.py
def levenshtein(a,b):
    "Calculates the Levenshtein distance between a and b."
    n, m = len(a), len(b)
    if n > m:
        # Make sure n <= m, to use O(min(n,m)) space
        a,b = b,a
        n,m = m,n
    current = range(n+1)
    for i in range(1,m+1):
        previous, current = current, [i]+[0]*n
        for j in range(1,n+1):
            # print a[j-1]
            # print b[i-1]
            add, delete = previous[j]+1, current[j-1]+1
            change = previous[j-1]
            if a[j-1] != b[i-1]:
                change += 1
            current[j] = min(add, delete, change)
    return current[n]

# returns matching word in lexicon, given a distance dist
def lexicon_filter(lexicon, word, dist):
	if not '_' in word:
		if word in lexicon:
			return word+'_0'
		else:
			for w in lexicon:
				if levenshtein(word, w) <= dist:
					return w+'_'+str(dist)
		return word
	else:
		return word

# returns the (first) word with minimal levenshtein distance from given word
def min_levenshtein(candidates, word):
	minlev = 99999
	r = ''
	for c in candidates:
		if levenshtein(c, word) < minlev:
			minlev = levenshtein(c, word)
			r = c
	return r

# simple cleaning - duplicate characters, non-alphabetic characters...
def clean(word):
	word = word.lower()
	word = re.sub(r'(.)\1\1+', r'\1', word)
	word = re.sub(r'[^[a-z]âäéèëêïîöôùüûç]', r'', word)
	return word

# tries to get the cleanest form of the given word within the corpus
def unify(word, corpus, wellformed_words):
	phone_list = []
	uni = clean(word)
	for x in wellformed_words:
		if uni in corpus and x in wellformed_words:
			if corpus[uni] == wellformed_words[x]:
				phone_list.append(x)
	if len(phone_list) == 1:
		print word.encode('utf-8') + ' −−> ' + phone_list[0].encode('utf-8')
	elif len(phone_list) > 1:
		print word.encode('utf-8') + ' −−> ' + min_levenshtein(phone_list, uni).encode('utf-8') + ', from [' + ','.join([e.encode('utf-8') for e in phone_list]) +']'


def main():
	corpus           = dict((e.split(',')[0], e.split(',')[1]) for e in utils.load_list('../../resources/mf_tweets_words.csv'))
	wellformed_words = dict((e.split(',')[0], e.split(',')[1]) for e in utils.load_list('../../resources/mf_tweets_words.csv') if e.split(',')[2] == 'YES')

	for word in set(corpus)-set(wellformed_words):
		unify(word, corpus, wellformed_words)





if __name__ == '__main__':
	main()