Maintain a proper directory hierarchy.

Use OOP as much as possible.

Document the code properly.

When pushing, don't add temporary files like .o and binaries.

:)


Ganga Batch- please follow these instructions:

Saiteja update the dynamic test.py code.

If you want to declare an identifier, first say "declare" then <TYPE> followed by <IDENTIFIER>
If you want a number, say "number" followed by the number.
In the python file, update your code such that if the user says "back", call the function Predict.assignCode() with the string in the window as argument. This is needed to update the code in the java file if the user says back.
If you want a variable in camel case say "camel" followed by <IDENTIFIER>. For only first letter capital, say "title" <ID> and for underscore say "under" <ID>. Test this part and change the words(say "under" to "underscore" if it works) if needed.
Assumption used: An identifier will always be at the end of an utterance. i.e. "print hello" is allowed but "print hello plus bye" is not allowed.
Also, need to test if plus comes as "+" or "plus".
Need to handle the case when the next tokens have ";" and the user says "enter". Currently, "enter" is not sent to the java file, but we need to do so to handle ";" - This thing to be done in the end.

Any queries, call Srinidhi/message on FB
