<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ page isELIgnored="false"%> 
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title>Build Metadata Service</title>
		<meta name="keywords" content="Build, Metadata" />
		<meta name="description" content="Manage Builds Binaries Info" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<%@ include file="../include/css.jsp"%>
	</head>

	<body class="skin-1">
		<!-- topbar starts -->
        <%@ include file="../include/top.jsp"%>
        <!-- topbar ends -->

		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<div class="main-container-inner">
				<a class="menu-toggler" id="menu-toggler" href="#">
					<span class="menu-text"></span>
				</a>

				<!-- topbar starts -->
                <%@ include file="../include/left.jsp"%>
                <!-- topbar ends -->

				<div class="main-content">
					<div class="breadcrumbs" id="breadcrumbs">
						<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>

						<ul class="breadcrumb">
							<li>
								<i class="icon-home home-icon"></i>
								<a href="#">Home</a>
							</li>
							<li class="active">Dashboard</li>
						</ul><!-- .breadcrumb -->

					</div>

					<div class="page-content">

						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->

								<div class="alert alert-block alert-success">
									Welcome to Cloud DevOps Platform! adfasfas11111
								</div>
                                
                                <form action="/ws/builds" method="post">
                                    artifactId: <input type="text" name="artifactId" size="50" value="helloworld"/><br>
                                    branchName: <input type="text" name="branch" size="50" value="master"/><br>
                                    buildMd5: <input type="text" name="md5" size="50" value="a935373d"/><br>
                                    version: <input type="text" name="version" size="50" value="0.1-20140921.073931-1"/><br>
                                    buildType: <input type="text" name="type" size="50" value="continuous"/><br>
                                    classifier: <input type="text" name="classifier" size="50" /><br>
                                    downloadUrl: <input type="text" name="downloadUrl" size="50" value="https://nexus.flysnow.org/nexus/content/repositories"/><br>
                                    gitRepoName: <input type="text" name="repoUrl" size="50" value="git@git.flysnow.org:flysnow/helloworld.git"/><br>
                                    groupId: <input type="text" name="groupId" size="50" value="org.flysnow.cloud"/><br>
                                    packaging: <input type="text" name="packaging" size="50" value="zip"/><br>
                                    <input type="submit" value="submit"/>
                                </form>

								<div class="hr hr32 hr-dotted"></div>

								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div><!-- /.main-content -->
			</div><!-- /.main-container-inner -->
		</div><!-- /.main-container -->

		<!-- topbar starts -->
        <%@ include file="../include/js.jsp"%>
        <!-- topbar ends -->
</body>
</html>