<h1>SnackTank's Hypertext Transfer Protocol Server</h1>
<p>
A stupid simple HTTP/1.0 server written in Java 1.8.0 <br>
This server is not intended to be used in production.

<h2>How to compile</h2>
Run the provided script
<blockquote>
	sh compile.sh
</blockquote>
Please note it only works on Unix systems.

<h2>Features</h2>
I. GET <br>
II. HEAD <br>
III. POST <br>
IV. PUT <br>
V. Multithreading

<h2>Behaviors</h2>
PUT is disabled by default, and POST is enabled by default. Both PUT and POST store files as ASCII only. Binary files are NOT supported at this time. POST requests most be done in the 'uploads' directory. The default file, if no file is requested (I.e. '/'), is index.html. 

<h2>TODO</h2>
I. LINK <br>
II. UNLINK <br>
III. DELETE <br>
IV. AUTH <br>
V. FRIENDLY CONFIG <br>
VI. STABLE MULTITHREADING <br>

<h2>License</h2>
MIT <br>
Copyright MMXXVI SnackTank
</p>
