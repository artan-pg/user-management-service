## Begin your Contribution
- Clone the repository :)
- Create a branch with a short and self-explanatory name in your clone.
- Checkout your new branch and add commits with detailed and descriptive commit logs.
- When yours are ready, first make sure that you're in sync with the upstream repository. Then
  create a pull (merge) request from the repository page.
- Follow up with the automated and manual code review process to merge your request in the main.

## Caveats
If the main is updated after you have crated your pull-request, please rebase your branch with all the latest commits on
top of main before submitting your pull-request again.

## General Guidelines
- Organize your codes in appropriate packages.
- Document your code thoroughly.
- Write lots and lots of tests.
- Test everything before creating the pull request.
- Pull changes from main before submitting your pull request.
- As a rule of thumb, **always** do a `rebase` unless someone else is simultaneously working on your branch.

## Workflow Guidelines
- Create branch based on the latest version of `develop` with an appropriate name.
- Develop!
- When you think the branch is ready for a merge request, please rebase your branch with the latest `develop`.
- Create a merge request with appropriate title and description.
- When a merge request resolves one specific task, mention it using the famous `Resolves {task_id}` format.

## Documentation Guidelines
- Document everything!
- Learn how to write a good javadoc:
  - [Javadoc coding standards](https://blog.joda.org/2012/11/javadoc-coding-standards.html)
  - [How to Write Doc Comments for the Javadoc Tool](https://www.oracle.com/technetwork/java/javase/tech/index-137868.html)

## Testing Guidelines
- Suppose you're going to write a test for class `com.example.Foo`, then your unit test should be named
  `com.example.FooTest` and your integration test should be named `com.example.FooIT`. Basically, test should be
  organized in the same package as *Subject Under Test* and have `Test` and `IT` suffixes for unit and integration
  tests, respectively.
- Checkout JUnit 5's [documentation](https://junit.org/junit5/docs/current/user-guide/)
- Learn how to write integration tests for Spring:
  - [Spring Boot Test Support](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- Checkout [XUnit Test Patterns](http://xunitpatterns.com)
