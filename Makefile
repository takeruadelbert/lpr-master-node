app_name=ester

build_dir=build
target_dir=$(build_dir)/$(app_name)
dot_env=.env
dot_env_example=.env.example

dir_deployment=deployment
dir_ssl=$(dir_deployment)/ssl
dir_plain=$(dir_deployment)/plain

build: build_maven env
	cp -r $(dir_plain)/* $(target_dir)

build_ssl: build_maven env
	cp -r $(dir_ssl)/* $(target_dir)

env: $(target_dir)
	cp $(dot_env) $(target_dir)/$(dot_env) || cp $(dot_env_example) $(target_dir)/$(dot_env) && echo "Using $(dot_env_example) as $(dot_env)"

build_maven: clean
	mvn -Dmaven.test.skip=true package
	mkdir -p $(target_dir)
	cp target/*.war $(target_dir)/app.war

clean:
	rm -rf $(target_dir)
	mvn clean
