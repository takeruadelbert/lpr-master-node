app_name=ester

build_dir=build
target_dir=$(build_dir)/$(app_name)
dot_env=.env
dot_env_example=.env.example

dir_deployment=deployment
dir_http=$(dir_deployment)/http
dir_https=$(dir_deployment)/https

build: clean build_maven env
	cp -r $(dir_http)/* $(target_dir)

build_ssl: clean build_maven env
	cp -r $(dir_https)/* $(target_dir)

env: $(target_dir)
	cp $(dot_env) $(target_dir)/$(dot_env) || cp $(dot_env_example) $(target_dir)/$(dot_env) && echo "Using $(dot_env_example) as $(dot_env)"

build_maven: clean
	mvn -Dmaven.test.skip=true package
	mkdir -p $(target_dir)
	cp target/*.war $(target_dir)/app.war

clean:
	rm -rf $(target_dir)
	mvn clean
