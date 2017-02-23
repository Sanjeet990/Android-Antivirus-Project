# Android-Antivirus-Project
This is an Antivirus project for Android that I created for my college project.

# How this application works?
First of all, this application dumps Hex of the file. Then it compares the Hex with the known virus signatures. If it matches, then the file is reported as malicious file.

# Is it a best practice?
No! Seriously No. Hex generation is a very very very slow process even in very fast and responsive systems. So, this approach is not feasible for an embedded system, specially with large files. Still, its a prototype, and it is intended for understanding the virus detection technique.

# Where can I find more signatures?
You can get it from online resources. One of such resource is <a href="https://github.com/DanieleDeSensi/Peafowl/blob/master/demo/http_pattern_matching/signatures.example">https://github.com/DanieleDeSensi/Peafowl/blob/master/demo/http_pattern_matching/signatures.example</a>

