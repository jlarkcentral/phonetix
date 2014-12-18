unificator
==========

*Some experiments on word unification:

	*Phonetisation of french words in noisy context - Modified version of Phonetic by E. Berger
	*Some tests on its use combined with edition distance

## The results so far

To achieve this disambiguation we test the hypothesis that somewhere in the corpus lies a correct spelling of each word.
- Phonetic (or Phonetix) gives you the phonetisation of a list of words.
- The current version of the python script takes on input a CSV file, with a simple format:

	*word,phonetisation,"YES" or "NO"

"YES" meaning that the word is present in a dictionary (for the given ressource we use Morphalou) and thus is correctly spelled.
http://www.cnrtl.fr/lexiques/morphalou/

- We then compare the phonetisation of a misspelled word with those of the correct ones, and takes the candidate which is nearer with respect to the Levenshtein distance.


Correctness-wise, the output is far from outstanding but the nature of the process implies a very limited amount of noise, as every candidate already exists in the corpus. Moreover the computation is quite fast when compared to a dictionary matching algorithm which wouldn't do much better.