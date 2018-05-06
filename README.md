# File Crypt

Simple software to encrypt files with AES-256.

## How to use

* Download the last release [here](https://github.com/alexzava/FileCrypt/releases)

**Encryption**
```
java -jar FileCrypt.jar encrypt <filename>
```

**Decryption**
```
java -jar FileCrypt.jar decrypt <filename>
```

## How to build

* Clone the repository
```
git clone https://github.com/Alexzava/FileCrypt.git
```

* Build
```
cd FileCrypt/src

javac -cp ../out/lib/*.jar CryptoAES.java

javac Main.java

jar cvfm ../out/FileCrypt.jar META-INF/MANIFEST.MF *.class
```

* FileCrypt.jar is located in the *"out"* folder

### Build with a different version of Bouncy Castle

* Replace the file *"out/lib/bcprov-jdk15on-159.jar"* with the new one.

* Edit the manifest in *"src/META-INF/MANIFEST.MF"*
```
Manifest-Version: 1.0
Main-Class: Main
Class-Path: lib/<THE_NEW_FILE.jar>
```

* Build

## How it works

* Generate 64 secure random bytes as **Salt**
* Generate 12 secure random bytes as **IV**
* Generate **Encryption Key** (EK) with PBKDF2
```
EK = PBKDF2(password, Salt, 50000, 256)
```
* Encrypt/Decrypt with **AES/GCM/NoPadding**

## Built With

* [Bouncy Castle](https://bouncycastle.org/) - Java implementation of cryptographic algorithms.

## Authors

[**Alexzava**](https://github.com/alexzava)


## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

**This software has been made for personal and educational purposes.**
