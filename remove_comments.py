import os
import re


project_path = "C:/Users/abhin/Projects/Learning-Disability-Detection-System"


comment_pattern = re.compile(r'^\s*#.*$')

for root, dirs, files in os.walk(project_path):
    for file in files:
        if file.endswith(".py"):
            file_path = os.path.join(root, file)
            
            with open(file_path, "r", encoding="utf-8") as f:
                lines = f.readlines()
            
            new_lines = []
            for line in lines:
                if not comment_pattern.match(line):
                    new_lines.append(line)
            
            with open(file_path, "w", encoding="utf-8") as f:
                f.writelines(new_lines)
            
            print(f"Cleaned {file_path}")
