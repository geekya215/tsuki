# tsuki
tsuki is a simple type derivator based on the hindley milner type system, it performs type inference by implementing algorithm j.

### Examples
```bash
tsuki> \x.x
=> a -> a
tsuki> \x.\y.x y
=> (a -> b) -> a -> b
tsuki> \m.\n.\f.\x. m f (n f x)
=> (a -> b -> c) -> (a -> d -> b) -> a -> d -> c
tsuki> \n.\f.\x. n (\g.\h. h (g f)) (\u.x) (\u.u)
=> (((a -> b) -> (b -> c) -> c) -> (d -> e) -> (f -> f) -> g) -> a -> e -> g
```

### Acknowledgments
- [Hindley–Milner type system](https://en.wikipedia.org/wiki/Hindley%E2%80%93Milner_type_system)
- [algorithm-j](https://github.com/jfecher/algorithm-j)
