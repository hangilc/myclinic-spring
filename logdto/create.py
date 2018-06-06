import io
import os
from jinja2 import Environment, FileSystemLoader
import sys

name = sys.argv[1]

prog_dir = os.path.abspath(os.path.dirname(__file__))
env = Environment(loader=FileSystemLoader(prog_dir), trim_blocks=True, lstrip_blocks=True)

template = env.get_template('TemplateCreated.java.txt')
output = template.render(name = name)
output_file = prog_dir + "/src/main/java/jp/chang/myclinic/logdto/practicelog/" + name + "Created.java"
with io.open(output_file, "w", encoding='utf8') as f:
    f.write(output)

template = env.get_template('TemplateUpdated.java.txt')
output = template.render(name = name)
output_file = prog_dir + "/src/main/java/jp/chang/myclinic/logdto/practicelog/" + name + "Updated.java"
with io.open(output_file, "w", encoding='utf8') as f:
    f.write(output)

template = env.get_template('TemplateDeleted.java.txt')
output = template.render(name = name)
output_file = prog_dir + "/src/main/java/jp/chang/myclinic/logdto/practicelog/" + name + "Deleted.java"
with io.open(output_file, "w", encoding='utf8') as f:
    f.write(output)


