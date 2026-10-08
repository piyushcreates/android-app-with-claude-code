"""Checks store/listing.md fields against Google Play character limits."""
import pathlib
import re
import sys

LIMITS = {"App name": 30, "Short description": 80, "Full description": 4000}

text = pathlib.Path(__file__).with_name("listing.md").read_text()
sections = dict(re.findall(r"^## (.+?) \(max \d+\)\n(.*?)(?=^## |\Z)", text, flags=re.S | re.M))

ok = True
for name, limit in LIMITS.items():
    body = sections.get(name, "").strip()
    status = "OK " if 0 < len(body) <= limit else "BAD"
    ok &= status == "OK "
    print(f"{status} {name}: {len(body)}/{limit}")
sys.exit(0 if ok else 1)
