import os
import re


project_path = "C:/Users/abhin/Projects/Learning-Disability-Detection-System"

comment_pattern = re.compile(r'/\*.*?\*/', re.DOTALL)

for root, dirs, files in os.walk(project_path):
    for file in files:
        if file.endswith(".css") or file.endswith(".scss"):
            file_path = os.path.join(root, file)

            with open(file_path, "r", encoding="utf-8") as f:
                content = f.read()

            new_content = re.sub(comment_pattern, "", content)

            with open(file_path, "w", encoding="utf-8") as f:
                f.write(new_content)

            print(f"Cleaned comments from {file_path}")
