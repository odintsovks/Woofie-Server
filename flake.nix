{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.05";
  };
  outputs = { self, nixpkgs }:
  let
    system = "x86_64-linux";
    pkgs = nixpkgs.legacyPackages.${system};
    start = pkgs.writeShellScriptBin "startScript" ''
      set -e
      rm -r .data
      ${pkgs.postgresql}/bin/initdb -D .data
      echo "max_connections = 100
unix_socket_directories = '/tmp/'
shared_buffers = 128MB
dynamic_shared_memory_type = posix
max_wal_size = 1GB
min_wal_size = 80MB
log_timezone = 'Europe/Moscow'
datestyle = 'iso, mdy'
timezone = 'Europe/Moscow'
lc_messages = 'en_US.UTF-8'
lc_monetary = 'en_US.UTF-8'
lc_numeric = 'en_US.UTF-8'
lc_time = 'en_US.UTF-8'
default_text_search_config = 'pg_catalog.english'" > .data/postgresql.conf
      ${pkgs.postgresql}/bin/pg_ctl -D .data start
      echo "
CREATE USER postgres WITH PASSWORD 'nik123' SUPERUSER;
CREATE DATABASE woofie_db WITH OWNER postgres;
      " | ${pkgs.postgresql}/bin/psql -h /tmp -d postgres
      ${pkgs.postgresql}/bin/pg_ctl -D .data stop
      ${pkgs.postgresql}/bin/postgres -D .data
    '';
  in
  {
    devShell.${system} = pkgs.mkShellNoCC {
      shellHook = ''
        export JDTLS_CONFIG='cmd = {"${pkgs.jdt-language-server}/bin/jdtls"}, init_options = { settings = {java = {imports = {gradle = {enabled = true, wrapper = {enabled = true, checksums = {{sha256 = "7d3a4ac4de1c32b59bc6a4eb8ecb8e612ccd0cf1ae1e99f66902da64df296172", allowed = true}}}}}}}}'
        export PGHOST=/tmp
        export PGDATABASE=postgres
      '';
      packages = with pkgs; [
        javaPackages.compiler.temurin-bin.jdk-21
        gradle
        postgresql
      ];

    };
    packages.${system}.default = start;
  };
}
