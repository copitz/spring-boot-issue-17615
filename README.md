# Sample for Spring Boot issue [#17615](https://github.com/spring-projects/spring-boot/issues/17615#issuecomment-514429855)

This is a sample to show the problems caused by inconsistent property name handling by the binder.

[ApplicationBinderTest](./src/test/java/com/example/ApplicationsBinderTest.java) shows the manual binding failing without and working with the work around to replace the generated property paths like `"foo.0.bar"` with `"foo[0].bar"`.

[ConfigurationPropertiesTest](./src/test/java/com/example/ConfigurationPropertiesTest.java) just shows that the binding works fine when handled by Spring ConfigurationPropertiesBinder.