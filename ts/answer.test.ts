import { answer } from './answer'
import test from 'tape'

test('the answer is 42', (t) => {
  t.equal(42, answer()   )
  t.end()
})

const foo = 42