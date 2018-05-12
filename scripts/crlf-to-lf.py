import sys

file = sys.argv[1]
contents = open(file, "r", encoding="utf-8").read()
with open(file, "w", encoding="utf-8", newline="\n") as f:
	f.write(contents)
