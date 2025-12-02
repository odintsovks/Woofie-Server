{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.05";
  };
  outputs = { self, nixpkgs }:
  let
    system = "x86_64-linux";
    pkgs = nixpkgs.legacyPackages.${system};
  in
  {
    devShell.${system} = pkgs.mkShellNoCC {
      shellHook = ''export JDTLS_CONFIG='cmd = {"${pkgs.jdt-language-server}/bin/jdtls"}, init_options = { settings = {java = {imports = {gradle = {enabled = true, wrapper = {enabled = true, checksums = {{sha256 = "7d3a4ac4de1c32b59bc6a4eb8ecb8e612ccd0cf1ae1e99f66902da64df296172", allowed = true}}}}}}}}' '';
      packages = with pkgs; [
        javaPackages.compiler.temurin-bin.jdk-21
        gradle
      ];
    };
  };
}
