import io
import os
from jinja2 import Environment, FileSystemLoader

prog_dir = os.path.abspath(os.path.dirname(__file__))
config_dir = prog_dir + "/../config"
name_config_file = config_dir + "/master-name.txt"
with io.open(name_config_file, 'r', encoding='utf8') as f:
	lines = f.readlines()
config_dict = { 's': [], 'k': [], 'd': [], 'a': [] }
kind_dict = { 's': "Shinryou", 'k': "Kizai", 'd': "Disease", 'a': "DiseaseAdj" }
prefix_set = set(config_dict.keys())
for line in lines:
	pre = line[0]
	if pre in prefix_set :
		name = line.split(",")[1]
		config_dict.get(pre).append(name)
env = Environment(loader=FileSystemLoader(prog_dir), trim_blocks=True, lstrip_blocks=True)
template = env.get_template('gencode-template.txt')
for pre in prefix_set:
	class_name = "Resolved" + kind_dict[pre] + "Map"
	names = config_dict[pre]
	entries = []
	for name in names:
		k = name
		k = k.replace("－", "")
		k = k.replace("（", "")
		k = k.replace("）", "")
		entries.append((k,name))
	output = template.render(class_name = class_name, entries=entries)
	output_file = prog_dir + "/src/main/java/jp/chang/myclinic/mastermap/generated/" + class_name + ".java"
	with io.open(output_file, "w", encoding='utf8') as f:
		f.write(output)


	



