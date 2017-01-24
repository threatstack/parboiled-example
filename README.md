# Parboiled2 Example
This is the full source code for a blog post on parboiled2.

It contains parboiled2 parsers for a simplified version of [Slack Assisted Search](https://get.slack.help/hc/en-us/articles/202528808-Search-for-messages-and-files).
The BNF for our simplified version is:
```
Input  = Term, [ Filter ];
Filter = SearchInChannel | SearchMessagesFrom | SearchOnDate
SearchInChannel = "in:", Term
SearchMessagesFrom = "from:", Term;
SearchOnDate = "on:", Term;
Term = { AlphaNumeric-Character };
```

## Running the examples
If you have a JVM/JDK installed you can run the tests:
```
./sbt test
```
Or you can open a REPL and play with the examples:
```
./sbt console
```
