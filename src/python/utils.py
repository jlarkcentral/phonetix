#!/usr/bin/env python2.7
# -*- coding: utf-8 -*-

'''

'''

import codecs
import xml.etree.ElementTree as ET
import itertools
from collections import defaultdict


##############
# GENERAL

def load_list(filename):
	return [line.strip() for line in codecs.open(filename, 'r','utf-8')]

def load_dict(filename, separator=',', key=0, value=1):
	return {line.split(separator)[key].strip(): line.split(separator)[value].strip() for line in codecs.open(filename, 'r','utf-8')}

def load_dict_of_dict(filename, separator=','):
	d = defaultdict(lambda: defaultdict(lambda: 0))
	for l in load_list(filename):
		if len(l.split(separator)) == 3:
			d[l.split(separator)[0]][l.split(separator)[1]] = l.split(separator)[2]
	return d

def write_list(l, filename):
	fout = codecs.open(filename, 'w', 'utf-8')
	for e in l:
		fout.write(e+'\n')
	fout.close()

def write_dict(l, filename, separator=','):
	fout = codecs.open(filename, 'w', 'utf-8')
	for e in d:
		fout.write(e+separator+d[e]+'\n')
	fout.close()

def print_list(alist):
	print ('\n').join([x.encode('utf-8') for x in alist])

def print_dict(adict):
	print ('\n').join([(x+', '+str(adict[x])).encode('utf-8') for x in adict])

def set_in_sets(*lists):
	sets = (set(x) for x in lists)
	return set.intersection(*sets)

def in_sets(*lists):
	sets = (set(x) for x in lists)
	return len(set.intersection(*sets))!=0

def len_in_sets(*lists):
	sets = (set(x) for x in lists)
	return len(set.intersection(*sets))


###############
# TAL

def fmes(v1,v2):
	return 0.0 if (v1+v2) == 0 else (float(2*v1*v2))/(v1+v2)

def find_synonyms(lexicon, wordnet='/home/feral/Documents/workspace/TAL_resources/wonef-fscore-0.1.xml'):
	extracted = defaultdict(lambda:set())
	wolf = ET.parse(wordnet)
	root = wolf.getroot()
	synsets = root.findall('SYNSET')
	for s in synsets:
		synonyms = [l.text for l in s.find('SYNONYM').findall('LITERAL')]
		for s in set(synonyms)&set(lexicon):
			extracted[s] |= set([x for x in synonyms if not x == s])
	return extracted

# if corpus is dict : corpus[verbatims] = <words>
def corpus_elements(corpus):
	return list(itertools.chain(*[corpus[verbatim] for verbatim in corpus]))

# if corpus is dict : corpus[verbatims] = <words>
def projection(lexicon, corpus):
	return list(set(corpus_elements(corpus))&set(lexicon))

# if corpus is dict : corpus[verbatims] = <words>
def distribution(lexicon, corpus):
	words = corpus_elements(corpus)
	return dict((x,words.count(x)) for x in set(words) if x in lexicon)

# if corpus is dict : corpus[verbatims] = <words>
def verbatim_distribution(lexicon, corpus):
	distrib = defaultdict(lambda:[])
	for verbatim in corpus:
		for w in set_in_sets(lexicon, corpus[verbatim]):
			distrib[w].append(verbatim)
	return distrib
