import os
import re


project_path = "C:/Users/abhin/Projects/Learning-Disability-Detection-System"


single_line_comment = re.compile(r'//.*?$' , re.MULTILINE)
multi_line_comment = re.compile(r'/\*.*?\*/', re.DOTALL)

for root, dirs, files in os.walk(project_path):
    for file in files:
        if file.endswith(".js"):
            file_path = os.path.join(root, file)

            with open(file_path, "r", encoding="utf-8") as f:
                content = f.read()

            
            content_no_comments = re.sub(single_line_comment, "", content)
            content_no_comments = re.sub(multi_line_comment, "", content_no_comments)

            with open(file_path, "w", encoding="utf-8") as f:
                f.write(content_no_comments)

            print(f"Cleaned comments from {file_path}")
