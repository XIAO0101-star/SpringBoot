
# Use a Java base image
FROM mcr.microsoft.com/devcontainers/java:0-17

# Install MariaDB client
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
   && apt-get install -y mariadb-client \
   && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# [Optional] Uncomment this section to install additional OS packages.
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
#     && apt-get -y install --no-install-recommends <your-package-list-here>

# [Optional] Uncomment this line to install global node packages.
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm install -g <your-package-here>" 2>&1