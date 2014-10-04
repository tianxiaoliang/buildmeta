<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
				<a href="index.html" title="" class="tip-bottom" data-original-title="Go to Home"><i class="fa fa-home"></i> Home</a>
				<a href="#">Build</a>
				<a href="#" class="current">New Build</a>
			</div>
            <div class="container-fluid">
				<div class="row">
					<div class="col-xs-12">
						<div class="widget-box">
							<div class="widget-title">
								<span class="icon">
									<i class="fa fa-align-justify"></i>									
								</span>
								<h5>New Build</h5>
							</div>
							<div class="widget-content nopadding">
								<form action="#" method="get" class="form-horizontal">
									<div class="form-group">
										<label class="col-sm-3 col-md-3 col-lg-2 control-label">ArtifactId :</label>
										<div class="col-sm-9 col-md-9 col-lg-10">
											<input type="text" class="form-control input-sm">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 col-md-3 col-lg-2 control-label">Version :</label>
										<div class="col-sm-9 col-md-9 col-lg-10">
											<input type="text" class="form-control input-sm">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 col-md-3 col-lg-2 control-label">GitRepo :</label>
										<div class="col-sm-9 col-md-9 col-lg-10">
											<input type="text" class="form-control input-sm">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 col-md-3 col-lg-2 control-label">Branch :</label>
										<div class="col-sm-9 col-md-9 col-lg-10">
											<input type="text" class="form-control input-sm">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 col-md-3 col-lg-2 control-label">Continuous :</label>
										<div class="col-sm-9 col-md-9 col-lg-10">
											<input type="text" class="form-control input-sm" placeholder="This is a placeholder...">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 col-md-3 col-lg-2 control-label">Description :</label>
										<div class="col-sm-9 col-md-9 col-lg-10">
											<textarea rows="5" class="form-control"></textarea>
										</div>
									</div>
									<div class="form-actions">
										<button type="submit" class="btn btn-primary btn-sm">Save</button> or <a class="text-danger" href="#">Cancel</a>
									</div>
								</form>
							</div>
						</div>						
					</div>
				</div>

				
			</div>
		
        </div>
		<!-- topbar starts -->
        <%@ include file="/views/include/js.jsp"%>
        <!-- topbar ends -->
</body>
</html>