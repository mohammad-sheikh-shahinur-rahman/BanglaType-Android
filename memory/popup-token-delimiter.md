---
name: popup-token-delimiter
description: How multi-codepoint conjunct popups work in keyboard layout XML (space-delimited tokens)
metadata:
  type: project
---

In keyboard layout XML (`app/src/main/res/xml/keys_letters_*.xml`), `popupCharacters` is normally split into
**one popup key per UTF-16 char**. To put a multi-codepoint cluster (e.g. the conjunct `র‍্য`, which includes a
ZWJ) on a single popup key, **separate tokens with a space**: `popupCharacters="র্ ্র র‍্য ৰ ৱ"`.

**Why:** Bengali conjuncts/precomposed forms span multiple codepoints; the old per-char split shattered them.
**How to apply:** A space anywhere in `popupCharacters` switches that key to whitespace-delimited token mode
(opt-in, so the other ~47 layouts with no spaces are unaffected). Multi-char tokens commit via
`OnKeyboardActionListener.onText(...)`; single-char tokens still use `onKey(code)`. Implemented in
`MyKeyboard` mini-keyboard constructor + `Key.popupKeyCount()`, and `MyKeyboardView` popup-commit path.

Separately: precomposed Bengali letters `য়`(U+09DF) `ড়`(U+09DC) `ঢ়`(U+09DD) are Unicode composition
exclusions — the other layouts store them **decomposed** (base+nukta), so their single-`code` shift-commit
silently drops the nukta. Use the **precomposed** single codepoint in `keyLabel`/`shiftedKeyLabel` so the
char commits correctly. The Probhat layout (`keys_letters_bangla_probhat.xml`) does this.
