#!/usr/bin/env bash
# Creates the Play upload key and writes keystore.properties, so the password is typed once (twice to confirm).
set -euo pipefail
cd "$(dirname "$0")/.."

KEYSTORE=keystore/gutelements-upload.jks
if [[ -f "$KEYSTORE" || -f keystore.properties ]]; then
  echo "$KEYSTORE or keystore.properties already exists — not overwriting." >&2
  echo "Delete them first if you really want a new key (only safe before your first Play upload)." >&2
  exit 1
fi

echo "Choose a password for the upload key (at least 6 characters). Nothing appears as you type."
while true; do
  read -rs -p "Password: " PW1; echo
  read -rs -p "Confirm password: " PW2; echo
  if [[ "$PW1" != "$PW2" ]]; then echo "Passwords don't match — try again."; continue; fi
  if (( ${#PW1} < 6 )); then echo "Too short — use at least 6 characters."; continue; fi
  if [[ "$PW1" == *\\* ]]; then echo "Please avoid the backslash character (\\)."; continue; fi
  break
done
unset PW2

echo
echo "Next, keytool asks for your name/organisation and location. Press Enter to skip any, then type 'yes'."
mkdir -p keystore
export GE_KEYSTORE_PW="$PW1"
keytool -genkeypair -v \
  -keystore "$KEYSTORE" \
  -alias upload \
  -keyalg RSA -keysize 4096 \
  -validity 10000 \
  -storepass:env GE_KEYSTORE_PW \
  -keypass:env GE_KEYSTORE_PW

# Check the key opens with this password before writing the config.
keytool -list -keystore "$KEYSTORE" -alias upload -storepass:env GE_KEYSTORE_PW >/dev/null

umask 077
cat > keystore.properties <<EOF
storeFile=$KEYSTORE
storePassword=$PW1
keyAlias=upload
keyPassword=$PW1
EOF
unset PW1 GE_KEYSTORE_PW

echo
echo "Done: created $KEYSTORE and keystore.properties."
echo "Back up $KEYSTORE and the password somewhere off this computer (e.g. a password manager)."
echo "Build the signed bundle with: ./gradlew :app:bundleRelease"
