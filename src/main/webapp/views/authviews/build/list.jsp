<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ page isELIgnored="false"%> 
<!DOCTYPE html>
<html lang="en">
	<!-- topbar starts -->
    <%@ include file="/views/include/head.jsp"%>
    <!-- topbar ends -->
	
	<body data-color="grey" class="flat"><div id="wrapper">
		<!-- topbar starts -->
	    <%@ include file="/views/include/top.jsp"%>
	    <!-- topbar ends -->
	
		<!-- sidebar starts -->
	    <%@ include file="/views/include/left.jsp"%>
	    <!-- sidebar ends -->
		
		<div id="content">
			<div id="breadcrumb">
				<a href="#" title="Go to Home" class="tip-bottom"><i class="fa fa-home"></i> Home</a>
				<a href="#" class="current">Builds</a>
			</div>
			<div id="row">
			    <div class="col-xs-12">
				<a href="/views/authviews/build/new.jsp" class="btn btn-inverse btn-sm"><span>New</span></a>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon">
								<i class="fa fa-th"></i>
							</span>
							<h5>Component Builds</h5>
						</div>
						<div class="widget-content nopadding">
							<table class="table table-bordered table-striped table-hover data-table">
								<thead>
									<tr>
										<th>ArtifactId</th>
										<th>Version</th>
										<th>GitRepo</th>
										<th>Branch</th>
										<th>Type</th>
										<th>Actions</th>
									</tr>
								</thead>
								<tbody>
								    <c:forEach var="build" items="${builds}">
                                        <tr>
                                            <td>${build.artifactId}</td>

                                            <td>
                                                <a href="/ws/builds/data?ids=${build.id}">${build.version}</a>
                                            </td>

                                            <td class="hidden-480">${build.repoUrl}</td>
                                            <td>${build.branch}</td>

                                            <td class="hidden-480">
                                                Continuous
                                            </td>
                                            <td>
                                                <div class="btn-group">
													<button data-toggle="dropdown" class="btn btn-inverse dropdown-toggle btn-xs">Actions<span class="caret"></span></button>
													<ul class="dropdown-menu dropdown-inverse">
														<li><a href="#">Detail</a></li>
														<li><a href="#">Delete</a></li>
													</ul>
												</div>
                                            </td>
                                        </tr>
                                    </c:forEach>
								</tbody>
								</table>  
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- js starts -->
        <%@ include file="/views/include/footer.jsp"%>
        <!-- js ends -->
		
		<script type="text/javascript">
			$(document).ready(function() {
			  var rawhref = window.location.href; //raw current url 
			  alert(rawhref)
			  $('#sidebar-builds').addClass('active');
			  $('#sidebar-builds').addClass('open');
			  $('#sidebar-builds-list').addClass('active');
			});
		</script>   
		
        <!-- js starts -->
        <%@ include file="/views/include/js.jsp"%>
        <!-- js ends --> 
        
        
	</body>
</html>


