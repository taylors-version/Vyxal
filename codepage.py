codepage = "λ¬∧⟑∨⟇÷«»°\n․⍎½∆øÏÔÇæʀʁɾɽÞƈ∞⫙ß⎝⎠ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~⎡⎣⨥⨪∺❝ð£¥§¦¡∂ÐřŠč√∖ẊȦȮḊĖẸṙ∑Ṡİ•\t"
codepage += "Ĥ⟨⟩ƛıĲĳĴĵĶķĸĹĺĻļĽľĿŀŁłŃńŅņŇňŉŊŋŌōŎŏŐőŒœŔŕŖŗŘŚśŜŝŞşšŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſƀƁƂƃƄƅƆƇƊƋƌƍƎ¢≈Ωªº"

commands = {
    '!': 'stack.push(len(stack))',
    '"': 'stack.shift(_RIGHT)',
    "'": 'stack.shift(_LEFT)',
    '$': 'stack.swap()',
    '%': 'rhs, lhs = stack.pop(2); stack.push(modulo(lhs, rhs))',
    '&': 'if VY_reg_reps % 2:VY_reg=stack.pop()\nelse:stack.push(VY_reg)\nVY_reg_reps += 1',
    '*': 'rhs, lhs = stack.pop(2); stack.push(multiply(lhs, rhs))',
    '+': 'rhs, lhs = stack.pop(2); stack.push(add(lhs, rhs))',
    ',': 'pprint(stack.pop()); printed = True',
    '-': 'rhs, lhs = stack.pop(2); stack.push(subtract(lhs, rhs))',
    '.': 'print(stack.pop(), end=""); printed = True',
    '/': 'rhs, lhs = stack.pop(2); stack.push(divide(lhs, rhs))',
    ':': 'top = stack.pop(); stack.push(top); stack.push(top)',
    '<': 'rhs, lhs = stack.pop(2); stack.push(int(rhs < lhs))',
    '=': 'rhs, lhs = stack.pop(2); stack.push(int(rhs == lhs))',
    '>': 'rhs, lhs = stack.pop(2); stack.push(int(rhs > lhs))',
    '?': 'stack.push(get_input())',
    'A': 'stack.push(stack.all())',
    'B': 'stack.push(int(stack.pop(), 2))',
    'C': 'stack.push("{}")',
    'D': 'top = stack.pop(); stack.push(top); stack.push(top); stack.push(top)',
    'E': 'x = stack.pop(); stack.push(eval(x))',
    'F': 'stack.do_filter(stack.pop())',
    'G': 'lhs, rhs = stack.pop(2); stack.push(math.gcd(rhs, lhs))',
    'H': 'stack.push(int(stack.pop(), 16))',
    'I': 'stack.push(int(stack.pop()))',
    'J': 'lhs, rhs = stack.pop(2); stack.push(str(rhs) + str(lhs))',
    'K': 'stack.push({})',
    'L': 'stack.push(len(stack.pop()))',
    'M': 'stack.do_map(stack.pop())',
    'N': 'top = stack.pop(); stack.push(to_number(top))',
    'O': 'lhs, rhs = stack.pop(2); stack.push(rhs.count(lhs))',
    'P': 'TODO',
    'Q': 'exit()',
    'R': 'TODO',
    'S': 'stack.push(str(stack.pop()))',
    'T': 'stack.push([n for n in stack.pop() if bool(n)])',
    'U': 'TODO',
    'V': 'stack.push("{}")',
    'W': 'lhs, rhs = stack.pop(2); stack.push(textwrap.wrap(rhs, lhs))',
    'X': 'if _context_level + 1 < _max_context_level: _context_level += 1',
    'Y': 'TODO',
    'Z': 'lhs, rhs = stack.pop(2); stack.push(list(zip(rhs, lhs)))',
    '^': 'stack.reverse()',
    '_': 'stack.pop()',
    '`': 'stack.push("{}")',
    'a': 'stack.push(any(x))',
    'b': 'stack.push(bin(x))',
    'c': 'lhs, rhs = stack.pop(2); stack.push(rhs in lhs)',
    'd': 'stack.push(stack.pop() * 2)',
    'e': 'lhs, rhs = stack.pop(2); stack.push(rhs ** lhs)',
    'f': 'stack.push(flatten(stack.pop())',
    'g': 'stack.push(VY_source[stack.pop()])',
    'h': 'stack.push(stack.pop()[0])',
    'i': 'lhs, rhs = stack.pop(2); stack.push(as_iter(rhs)[lhs])',
    'j': 'lhs, rhs = stack.pop(2); stack.push(lhs.join([str(_item) for _item in as_iter(rhs)])); ',
    'l': 'stack.push([])',
    'm': 'TODO',
    'n': 'stack.push(eval(f"_context_{_context_level}"))',
    'o': 'stack.push(type(stack.pop()))',
    'p': 'TODO',
    'q': 'stack.push('"' + str(stack.pop()) + '"')',
    'r': 'lhs, rhs = stack.pop(2); stack.push(list(range(rhs, lhs)))',
    's': 'top = stack.pop(); stack.push(sorted(as_iter(top)))',
    't': 'stack.push(stack.pop()[-1])',
    'u': 'TODO',
    'w': 'stack.push([stack.pop()])',
    'x': '_context_level -= 1 * (1 - (_context_level == 0))',
    'y': 'TODO',
    'z': 'TODO',
    '~': 'stack.push(random.randint(-INT, INT))',
    '¬': 'stack.push(not stack.pop())',
    '∧': 'lhs, rhs = stack.pop(2); stack.push(bool(rhs and lhs))',
    '⟑': 'lhs, rhs = stack.pop(2); stack.push(rhs and lhs)',
    '∨': 'lhs, rhs = stack.pop(2); stack.push(bool(rhs or lhs))',
    '⟇': 'lhs, rhs = stack.pop(2); stack.push(rhs or lhs)',
    '÷': 'for item in stack.pop(): stack.push(item)',
    '⍎': 'stack += (stack.pop())(stack)',
    'Ṛ': 'lhs, rhs = stack.pop(2); stack.push(random.randint(rhs, lhs))',
    'Ï': 'lhs, rhs = stack.pop(2); stack.push(as_iter(rhs).index(lhs))',
    'Ô': 'TODO',
    'Ç': 'TODO',
    'ʀ': 'stack.push(list(range(0, stack.pop() + 1)))',
    'ʁ': 'stack.push(list(range(0, stack.pop())))',
    'ɾ': 'stack.push(list(range(1, stack.pop() + 1)))',
    'ɽ': 'stack.push(list(range(1, stack.pop())))',
    'Þ': 'top = as_iter(stack.pop()); stack.push(top == top[::-1])',
    'ƈ': 'TODO',
    '∞': 'TODO',
    'ß': 'TODO',
    '∺': 'stack.push(stack.pop() % 2)',
    "∻": 'lhs, rhs = stack.pop(2); stack.push(int((rhs % lhs) == 0))',
    '\n': '',
    '\t': '',
    "Ĥ": "stack.push(100)",
    "Ĵ": "stack.push(''.join(stack.pop())",
    "Ĳ": "stack.push('\\n'.join(stack.pop()))",
    "ĳ": "stack.push(10)",
    "ĵ": "x = stack.pop(); stack.push(x * x)",
    "∑": "stack.push(sum(stack.pop()))" # TODO: Ensure that this works with lists of mixed types. Essentially, reduction by addition.ß
    }
