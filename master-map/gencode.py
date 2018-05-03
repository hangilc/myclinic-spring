import io
from jinja2 import Environment, FileSystemLoader

name_config_file = "../config/master-name.txt"

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

env = Environment(loader=FileSystemLoader('.'))
template = env.get_template('gencode-template.txt')
for pre in prefix_set:
	class_name = "Resolved" + kind_dict[pre] + "Map"
	names = config_dict[pre]
	output = template.render(class_name = class_name, names=names)
	output_file = "./src/main/java/jp/chang/myclinic/mastermap/generated/" + class_name + ".java"
	with io.open(output_file, "w", encoding='utf8') as f:
		f.write(output)


	



