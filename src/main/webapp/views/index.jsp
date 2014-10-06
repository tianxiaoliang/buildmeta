<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@ page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${!empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION}">
  <c:redirect url="/views/login.jsp"/>
</c:if>
					    
<!DOCTYPE html>
<html lang="en">
	<!-- head starts -->
    <%@ include file="/views/include/head.jsp"%>
    <!-- head ends -->
	<body data-color="grey" class="flat">
		<div id="wrapper">
		    <!-- topbar starts -->
	        <%@ include file="/views/include/top.jsp"%>
	        <!-- topbar ends -->
        
			<!-- sidebar starts -->
            <%@ include file="/views/include/left.jsp"%>
            <!-- sidebar ends -->
			
			<!-- content starts -->
			<div id="content">
				<div id="content-header" class="mini">
					<h1>Dashboard</h1>
					<ul class="mini-stats box-3">
						<li>
							<div class="left sparkline_bar_good"><span>2,4,9,7,12,10,12</span>+10%</div>
							<div class="right">
								<strong>36094</strong>
								CodeChanges
							</div>
						</li>
						<li>
							<div class="left sparkline_bar_neutral"><span>20,15,18,14,10,9,9,9</span>0%</div>
							<div class="right">
								<strong>1433</strong>
								Builds
							</div>
						</li>
						<li>
							<div class="left sparkline_bar_bad"><span>3,5,9,7,12,20,10</span>+50%</div>
							<div class="right">
								<strong>8650</strong>
								Upgrades
							</div>
						</li>
					</ul>
				</div>
				<div id="breadcrumb">
					<a href="#" title="Go to Home" class="tip-bottom"><i class="fa fa-home"></i> Home</a>
					<a href="#" class="current">Dashboard</a>
				</div>
				
				<!-- container-fluid starts -->
				<div class="container-fluid">
				    <!-- row starts -->
					<div class="row">
						<div class="col-xs-12">
							<div class="widget-box">
								<div class="widget-title">
									<span class="icon"><i class="fa fa-signal"></i></span>
									<h5>Site Statistics</h5>
									<div class="buttons">
										<a href="#" class="btn"><i class="fa fa-refresh"></i> <span class="text">Update stats</span></a>
									</div>
								</div>
								<div class="widget-content">
									<div class="row">
										<div class="col-xs-12 col-sm-4">
											<ul class="site-stats">
												<li><div class="cc"><i class="fa fa-user"></i> <strong>143</strong> <small>Total Farms</small></div></li>
												<li><div class="cc"><i class="fa fa-arrow-right"></i> <strong>16</strong> <small>Upgrades (last week)</small></div></li>
												<li class="divider"></li>
												<li><div class="cc"><i class="fa fa-shopping-cart"></i> <strong>15000</strong> <small>Code Changes</small></div></li>
												<li><div class="cc"><i class="fa fa-repeat"></i> <strong>29</strong> <small>Pending Upgrades</small></div></li>
											</ul>
										</div>
										<div class="col-xs-12 col-sm-8">
											<div class="chart"></div>
										</div>	
									</div>							
								</div>
							</div>					
						</div>
					</div>
					<!-- row ends -->
				</div>
                <!-- container-fluid starts -->
			</div>
			<!-- contents ends -->
			
			<!-- footer starts -->
	        <%@ include file="/views/include/footer.jsp"%>
	        <!-- footer ends -->
		</div>

        <!-- js starts -->
        <%@ include file="/views/include/js.jsp"%>
        <!-- js ends -->
	</body>
</html>
