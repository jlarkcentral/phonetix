#!/usr/bin/env python2.7
# -*- coding: utf-8 -*-

'''
unification experiments
'''

import utils
import sys
import os
import re
import string
from collections import defaultdict

eng_words = utils.load_list('/home/feral/Documents/workspace/TAL_resources/wordnet/dict/all_indexes')
morphalouphone = dict((x.split(',')[0], x.split(',')[1].split(';')) for x in utils.load_list('/home/feral/Documents/workspace/TAL_resources/morphalou/morphalou_phones'))

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

# returns the (first) word with minimal levenshtein distance from given word
def min_levenshtein(candidates, word):
	if len(candidates) == 0:
		return word
	elif len(candidates) == 1:
		return candidates[0]
	else:
		minlev = 99999
		r = ''
		for c in candidates:
			if levenshtein(c, word) < minlev:
				minlev = levenshtein(c, word)
				r = c
		return r

# returns list of similar candidates by phoneme if leven
# of word is less than the given distance
def leven_phone(word, phone, wellformed_words, dist):
	plist = []
	for w in wellformed_words:
		if levenshtein(word, w) <= dist and phone == wellformed_words[w]:
			plist.append(w)
	return plist

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

# simple cleaning - duplicate characters, non-alphabetic characters...
def clean(word):
	word = word.lower()
	word = re.sub(r'(.)\1\1+', r'\1', word)
	word = re.sub(r'[^[a-z]âäéèëêïîöôùüûç]', r'', word)
	return word

# tries to get the cleanest form of the given word within the corpus
# return the best candidates and the list of candidates
def unify(word, corpus, wellformed_words, dist):
	phone_list = []
	uni = clean(word) if clean(word) in corpus else word
	for x in wellformed_words:
		if corpus[uni] == wellformed_words[x]:
			phone_list.append(x)
#	if not phone_list:
#		phone_list = leven_phone(uni, corpus[uni], wellformed_words, dist)
	if not phone_list:
		if corpus[uni] in morphalouphone:
			print 'USING MORPHALOUPHONE'
			phone_list = morphalouphone[corpus[uni]]
	return (min_levenshtein(phone_list, uni), phone_list)
	

def main():
	path_words       = sys.argv[1] if len(sys.argv) == 2 else os.path.dirname(os.path.realpath(__file__))\
								+'/../../resources/mf_tweets_words.csv'
	corpus_words     = dict((e.split(',')[0], e.split(',')[1]) for e in utils.load_list(path))
	wellformed_words = dict((e.split(',')[0], e.split(',')[1]) for e in utils.load_list(path) if e.split(',')[2] == 'YES')

	for word in set(corpus)-(set(wellformed_words)|set(eng_words)):
		u,l = unify(word, corpus, wellformed_words, 1)
		if len(l) <= 1:
			print word.encode('utf-8') + ' −−> ' + u.encode('utf-8')
		else:
			print word.encode('utf-8') + ' −−> ' + u.encode('utf-8') + ', from [' + ','.join([e.encode('utf-8') for e in l]) +']'


	# for sentence evaluation
	path_sentences = '/home/feral/Documents/workspace/TAL_resources/mf_twit_all.txt'
	corpus_sentences = utils.load_list(path_sentences)
	corpus_segmented = list(itertools.chain(*[x.split(' ') for x in corpus_sentences]]))

	


if __name__ == '__main__':
	main()
