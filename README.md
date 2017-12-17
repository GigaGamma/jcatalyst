# jcatalyst
Dynamic web templating made simple

## Installation
Currently, the only way to install JCatalyst is to install it from the releases section or compile it yourself. JCatalyst will be available on Maven as soon as posssible.

## Getting Started
```java
public static void main(String[] args) {
	get("/", (req, res) -> {
		Map<String, Object> page_args = new HashMap<>();
		return new View("index.html", page_args);
	});
}
```

## HTML Dynamic Templating
```html
<h1 data-catalyst="example-value"></h1>
<input data-catalyst="example-value">
```

Look at our [default example](https://github.com/GigaGamma/jcatalyst/blob/master/JCatalyst/src/com/jcatalyst/app/templates/index.html) to learn more.